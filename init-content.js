/**
 * Script to populate the database with test content
 * Creates 5 films with categories and content
 */

const API_BASE = 'http://localhost:8090/api';
const AUTH_TOKEN = ''; // Will be set after login

// Helper function for API calls
async function apiCall(method, endpoint, body = null) {
  const options = {
    method,
    headers: {
      'Content-Type': 'application/json',
    },
  };

  if (AUTH_TOKEN) {
    options.headers['Authorization'] = `Bearer ${AUTH_TOKEN}`;
  }

  if (body) {
    options.body = JSON.stringify(body);
  }

  try {
    const response = await fetch(`${API_BASE}${endpoint}`, options);
    const data = await response.json();
    return { success: response.ok, data, status: response.status };
  } catch (error) {
    console.error(`API call failed: ${method} ${endpoint}`, error.message);
    return { success: false, error: error.message };
  }
}

// 1. Register admin user
async function setupUser() {
  console.log('📝 Setting up admin user...');
  const result = await apiCall('POST', '/auth/register', {
    username: 'admin',
    email: 'admin@example.com',
    password: 'admin123',
  });

  if (!result.success && result.status !== 409) {
    console.error('❌ Failed to create user:', result.data);
    return false;
  }

  console.log('✅ User ready');
  return true;
}

// 2. Login to get token
async function login() {
  console.log('🔐 Logging in...');
  const result = await apiCall('POST', '/auth/login', {
    username: 'admin',
    password: 'admin123',
  });

  if (!result.success) {
    console.error('❌ Login failed:', result.data);
    return false;
  }

  global.AUTH_TOKEN = result.data.token;
  console.log('✅ Logged in successfully');
  return true;
}

// 3. Create categories
async function createCategories() {
  console.log('📂 Creating categories...');

  const categories = ['Action', 'Comedy', 'Drama', 'Horror', 'Sci-Fi'];
  const categoryIds = [];

  for (const name of categories) {
    const result = await apiCall('POST', '/api/categories', { name });
    if (result.success) {
      categoryIds.push(result.data.id);
      console.log(`  ✓ Created: ${name}`);
    }
  }

  return categoryIds;
}

// 4. Create content (Films)
async function createContent(categoryIds) {
  console.log('🎬 Creating test content...');

  const films = [
    {
      title: 'The Matrix',
      description: 'A computer hacker learns about the true nature of his reality and his role in the war against its controllers.',
      releaseDate: '1999-03-31',
      categoryId: categoryIds[3], // Action
      durationInMinutes: 136,
      director: 'Lana Wachowski',
    },
    {
      title: 'Forrest Gump',
      description: 'The presidencies of Kennedy, Johnson, and Nixon unfold from the perspective of an Alabama man with an IQ of 75.',
      releaseDate: '1994-07-06',
      categoryId: categoryIds[1], // Comedy
      durationInMinutes: 142,
      director: 'Robert Zemeckis',
    },
    {
      title: 'Inception',
      description: 'A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea.',
      releaseDate: '2010-07-16',
      categoryId: categoryIds[4], // Sci-Fi
      durationInMinutes: 148,
      director: 'Christopher Nolan',
    },
    {
      title: 'The Shawshank Redemption',
      description: 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.',
      releaseDate: '1994-10-14',
      categoryId: categoryIds[2], // Drama
      durationInMinutes: 142,
      director: 'Frank Darabont',
    },
    {
      title: 'Parasite',
      description: 'Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.',
      releaseDate: '2019-05-30',
      categoryId: categoryIds[2], // Drama
      durationInMinutes: 132,
      director: 'Bong Joon-ho',
    },
  ];

  for (const film of films) {
    const result = await apiCall('POST', '/api/contents/films', film);
    if (result.success) {
      console.log(`  ✓ Created: ${film.title}`);
    } else {
      console.error(`  ✗ Failed to create ${film.title}:`, result.data);
    }
  }

  console.log('✅ Content creation complete');
}

// Main execution
async function main() {
  console.log('🚀 Starting database population...\n');

  if (!(await setupUser())) return;
  if (!(await login())) return;

  const categoryIds = await createCategories();
  if (categoryIds.length === 0) return;

  await createContent(categoryIds);

  console.log('\n✨ Database population complete! The application is ready to use.');
  console.log('📱 Frontend: http://localhost:4200');
  console.log('🔌 Backend: http://localhost:8090');
}

main().catch(console.error);
