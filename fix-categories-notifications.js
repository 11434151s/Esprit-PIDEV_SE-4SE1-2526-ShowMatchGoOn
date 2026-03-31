// Fix script to properly populate categories and notifications with complete data
const { MongoClient } = require('mongodb');

const MONGO_URI = 'mongodb://localhost:27017';
const DB_NAME = 'content_management_db';

async function fixData() {
  const client = new MongoClient(MONGO_URI);
  
  try {
    await client.connect();
    const db = client.db(DB_NAME);
    
    console.log('Connected to MongoDB...');
    
    // Fix categories - add descriptions to all categories
    const categoriesCollection = db.collection('categorys');
    
    console.log('\n=== Fixing Categories ===');
    const categoryUpdates = [
      { name: 'Action', description: 'High-octane films featuring explosions, stunts, and intense sequences' },
      { name: 'Drama', description: 'Character-driven stories exploring human emotions and complex relationships' },
      { name: 'Science Fiction', description: 'Imaginative stories set in futuristic worlds with advanced technology' },
      { name: 'Documentary', description: 'Real-world stories and educational content about varied topics' },
      { name: 'Comedy', description: 'Humorous films designed to entertain and make audiences laugh' }
    ];
    
    for (const update of categoryUpdates) {
      const result = await categoriesCollection.updateOne(
        { name: update.name },
        { $set: { description: update.description } },
        { upsert: true }
      );
      console.log(`✓ Updated/Created category: ${update.name}`);
    }
    
    // Verify categories
    const categories = await categoriesCollection.find({}).toArray();
    console.log(`\n✓ Total categories: ${categories.length}`);
    categories.forEach(cat => {
      console.log(`  - ${cat.name}: ${cat.description}`);
    });
    
    // Fix notifications - create sample notifications
    const notificationsCollection = db.collection('notifications');
    const usersCollection = db.collection('users');
    
    console.log('\n=== Fixing Notifications ===');
    
    // Get a user ID for notifications
    const user = await usersCollection.findOne({});
    if (!user) {
      console.log('! No users found, creating default user...');
      const defaultUser = {
        username: 'admin',
        email: 'admin@example.com',
        password: 'hashed_password',
        roles: ['ADMIN'],
        createdAt: new Date()
      };
      const userResult = await usersCollection.insertOne(defaultUser);
      console.log(`✓ Created default user with ID: ${userResult.insertedId}`);
    }
    
    const userId = user ? user._id : (await usersCollection.findOne({}))._id;
    
    // Clear existing notifications
    await notificationsCollection.deleteMany({});
    console.log('✓ Cleared existing notifications');
    
    // Create sample notifications
    const sampleNotifications = [
      {
        message: 'New film added: The Shawshank Redemption',
        type: 'INFO',
        isRead: false,
        user: { _id: userId, username: 'admin' },
        userId: userId.toString(),
        createdAt: new Date(Date.now() - 1000 * 60 * 5) // 5 mins ago
      },
      {
        message: 'New category created: Comedy',
        type: 'INFO',
        isRead: false,
        user: { _id: userId, username: 'admin' },
        userId: userId.toString(),
        createdAt: new Date(Date.now() - 1000 * 60 * 15) // 15 mins ago
      },
      {
        message: 'System update completed successfully',
        type: 'SUCCESS',
        isRead: true,
        user: { _id: userId, username: 'admin' },
        userId: userId.toString(),
        createdAt: new Date(Date.now() - 1000 * 60 * 60) // 1 hour ago
      },
      {
        message: 'Warning: Database size is growing',
        type: 'WARNING',
        isRead: false,
        user: { _id: userId, username: 'admin' },
        userId: userId.toString(),
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 2) // 2 hours ago
      }
    ];
    
    const notificationResult = await notificationsCollection.insertMany(sampleNotifications);
    console.log(`✓ Created ${notificationResult.insertedIds.length} notifications`);
    
    // Verify notifications
    const notifications = await notificationsCollection.find({}).toArray();
    console.log(`\n✓ Total notifications: ${notifications.length}`);
    notifications.forEach(notif => {
      console.log(`  - [${notif.type}] ${notif.message} (read: ${notif.isRead})`);
    });
    
    console.log('\n✓ All data fixed successfully!');
    
  } catch (error) {
    console.error('Error:', error);
  } finally {
    await client.close();
  }
}

fixData();
