# Complete Testing Guide - Step by Step

## Prerequisites
- Backend running on `http://localhost:8080`
- Frontend running on `http://localhost:4200`
- Browser with developer tools (F12)

---

## Step 1: Start the Backend Server

### Option A: Using Maven (Recommended)
```bash
cd c:\Users\SBS\Desktop\SMGO\backend
mvn spring-boot:run
```

**Expected output:**
```
[INFO] Started ContentManagementApplication in X seconds
```

**You should see:**
- No error messages
- Application running on port 8080
- Database connection established

### Option B: Using Pre-built JAR
```bash
cd c:\Users\SBS\Desktop\SMGO\backend\target
java -jar content-management-0.0.1-SNAPSHOT.jar
```

**Verify Backend is Running:**
- Open browser and go to: `http://localhost:8080`
- You may see a 404 page (that's OK - Spring Boot is running)
- Check console shows "Started ContentManagementApplication"

---

## Step 2: Build the Frontend

```bash
cd c:\Users\SBS\Desktop\SMGO\frontend
npm run build
```

**Expected output:**
```
Application bundle generation complete. [X.XXX seconds]
Output location: C:\Users\SBS\Desktop\SMGO\frontend\dist\show-match-go-on
```

**Check:**
- No errors in console
- Build size around 475 KB
- Exit code: 0

---

## Step 3: Start the Frontend Development Server

```bash
cd c:\Users\SBS\Desktop\SMGO\frontend
npm start
```

**Expected output:**
```
✔ Compiled successfully.
✔ Application bundle generation complete [X.XXX seconds]
```

**Wait for:**
- "✔ Compiled successfully" message
- Takes about 30-60 seconds to start

---

## Step 4: Open the Application

1. **Open your browser**
2. **Navigate to:** `http://localhost:4200`
3. **You should see:**
   - The SMGO application dashboard
   - Navigation menu on the left
   - Admin sections

---

## Step 5: Test Content Management (CRUD)

### 5.1 Test CREATE (Add Content)

**Steps:**
1. Click **"Content Management"** in the sidebar
2. Click **"Add Content"** button (purple button, top right)
3. A modal form should open

**Fill the form:**
```
Title:          Test Movie 1
Description:    This is a test movie description for testing
Release Date:   2026-03-15
Duration:       120
Genre:          Action
Status:         Active
Hidden Gem:     Yes (check the box)
```

**Click "Save"**

**Expected Result:**
- ✅ Alert says "Content created successfully!"
- ✅ Modal closes
- ✅ New content appears in the table
- ✅ "Total Content" count increases

**If Error:**
- Check browser console (F12)
- Verify backend is running
- Check error message on page
- See troubleshooting section below

---

### 5.2 Test READ (View Content)

**Automatic on page load:**
1. Go to **Content Management**
2. Wait 2-3 seconds
3. All content should load automatically

**Expected Result:**
- ✅ Table populates with content items
- ✅ Stats cards show counts
- ✅ No error messages

**If No Content Shows:**
- Check backend console for errors
- Verify API connection in browser console
- Try refreshing page

---

### 5.3 Test UPDATE (Edit Content)

**Steps:**
1. Go to **Content Management**
2. Look at the content table
3. Find the item you just created
4. Click the **edit icon** (pencil button) on the right
5. Modal opens with data pre-filled

**Update the form:**
```
Title:          Test Movie 1 - Updated
Duration:       150
Status:         Hidden
```

**Click "Save"**

**Expected Result:**
- ✅ Alert says "Content updated successfully!"
- ✅ Table updates immediately
- ✅ Values reflect your changes

---

### 5.4 Test DELETE (Remove Content)

**Steps:**
1. Go to **Content Management**
2. Find the test item you created
3. Click **delete icon** (trash button)
4. Confirmation dialog appears

**Click "OK" to confirm**

**Expected Result:**
- ✅ Alert says "Content deleted successfully!"
- ✅ Item disappears from table
- ✅ "Total Content" count decreases

---

## Step 6: Test Form Validation

### 6.1 Test Required Fields

**Steps:**
1. Click **"Add Content"** button
2. Leave all fields empty
3. Submit the form by clicking "Save"

**Expected Result:**
- ❌ Save button is disabled (grayed out)
- ✅ Cannot click Save button

---

### 6.2 Test Text Length Validation

**Steps:**
1. Click **"Add Content"** button
2. Enter in Title field: `ab` (only 2 characters)
3. Leave other fields empty

**Expected Result:**
- ❌ Title field has red border
- ✅ Error message appears: "Title must be at least 3 characters"
- ❌ Save button remains disabled

**Try with long text:**
1. Clear the title field
2. Paste a very long text (256+ characters)

**Expected Result:**
- ❌ Red border around title
- ✅ Error message: "Title must not exceed 255 characters"

---

### 6.3 Test Date Validation

**Steps:**
1. Click **"Add Content"** button
2. Click "Release Date" field
3. Try to select a date in the past (e.g., 2024-01-01)

**Expected Result:**
- ❌ Date field shows red border
- ✅ Error message: "Release date cannot be in the past"
- ❌ Save button disabled

**Correct Date:**
1. Select a future date (e.g., 2026-04-01)

**Expected Result:**
- ✅ Red border disappears
- ✅ Error message clears

---

### 6.4 Test Number Validation

**Steps:**
1. Click **"Add Content"** button
2. Click "Duration" field
3. Try to enter: `0` or `-5`

**Expected Result:**
- ❌ Duration field has red border
- ✅ Error message: "Duration must be at least 1"

**Try entering text:**
1. Clear duration field
2. Type: `abc`

**Expected Result:**
- ❌ Red border
- ✅ Error message: "Duration must be a valid number"

**Correct Value:**
1. Enter: `120`

**Expected Result:**
- ✅ Border turns normal
- ✅ Error clears

---

## Step 7: Test Search & Filter

**Steps:**
1. Go to **Content Management**
2. Wait for content to load
3. Locate the search bar at the top

**Test Search:**
1. Type in search box: `test`
2. Press Enter or wait (auto-filters)

**Expected Result:**
- ✅ Only items with "test" in title show
- ✅ Other items disappear

**Clear Search:**
1. Clear the search box
2. All items reappear

**Test Status Filter:**
1. Use "All Status" dropdown
2. Select "Active"

**Expected Result:**
- ✅ Only active items show
- ✅ Hidden and scheduled items disappear

---

## Step 8: Test Category Management

**Steps:**
1. Click **"Category Management"** in sidebar
   (Note: This is labeled "Cinema Partners" but manages categories)
2. Click **"Add Category"** button

**Fill the form:**
```
Name:           Action
Description:    All action-packed movies and shows
```

**Click "Save"**

**Expected Result:**
- ✅ Alert: "Category created successfully!"
- ✅ New category appears in table
- ✅ "Total Categories" count increases

**Test Edit:**
1. Click edit icon on a category
2. Change name to: `Action Movies`
3. Click Save

**Expected Result:**
- ✅ Alert: "Category updated successfully!"
- ✅ Table updates

**Test Delete:**
1. Click delete icon
2. Confirm deletion

**Expected Result:**
- ✅ Alert: "Category deleted successfully!"
- ✅ Item removed from list

---

## Step 9: Test Notifications

**Steps:**
1. Click **"Notification Hub"** in sidebar
2. Click **"Send Notification"** button

**Fill the form:**
```
Title:          Test Alert
Message:        This is a test notification message for the system
```

**Click "Send"**

**Expected Result:**
- ✅ Alert: "Notification sent successfully!"
- ✅ Notification appears at top of list

**Test Mark as Read:**
1. Find the notification in the list
2. It should show as unread

**Test Delete:**
1. Click delete icon on notification
2. Confirm deletion

**Expected Result:**
- ✅ Notification removed

---

## Step 10: Test Error Handling

### 10.1 Test Backend Connection Error

**Steps:**
1. Stop the backend server (Ctrl+C in backend terminal)
2. Go to **Content Management**
3. Click "Add Content"
4. Fill the form completely
5. Click "Save"

**Expected Result:**
- ✅ Error message appears on page
- ✅ Error text says something like: "Failed to create content"
- ✅ Modal stays open
- ✅ No crash or broken page

**Restart Backend:**
1. Start backend again
2. Try saving again
3. Should work now

### 10.2 Test Browser Console

**Steps:**
1. Press **F12** to open Developer Tools
2. Click **"Console"** tab
3. Try creating content

**Expected Result:**
- ✅ No red error messages
- ✅ May see network requests in Network tab
- ✅ Request to `http://localhost:8080/api/contents/films`

**How to Read Network Tab:**
1. Click **"Network"** tab
2. Create content
3. Look for request to `/api/contents/films`
4. Click it to see details
   - Status should be 200 or 201 (success)
   - Response shows created object

---

## Step 11: Complete Test Scenario

**This comprehensive test covers everything:**

1. ✅ Add a Film (test CREATE)
2. ✅ Add a Series (test different content type)
3. ✅ Search for the film (test READ + SEARCH)
4. ✅ Edit the film (test UPDATE)
5. ✅ Try submitting empty form (test VALIDATION)
6. ✅ Add a Category (test Category CRUD)
7. ✅ Send a Notification (test Notification CRUD)
8. ✅ Delete the film (test DELETE)
9. ✅ Verify all changes reflected (test STATE MANAGEMENT)
10. ✅ Check browser console for errors (test ERROR HANDLING)

---

## Troubleshooting

### Problem: Frontend shows "Cannot GET /"
**Solution:**
- Backend is not running
- Start backend: `mvn spring-boot:run` in backend folder
- Wait for "Started ContentManagementApplication"

### Problem: "Failed to load content" error
**Solution:**
1. Open browser console (F12)
2. Check Network tab
3. Look for failed requests to `localhost:8080`
4. Verify backend is running
5. Check backend logs for errors

### Problem: Form won't submit (Save button disabled)
**Solution:**
- Read error messages on form
- Fix all red-bordered fields
- Ensure all required fields have values
- Check validation error messages

### Problem: Content doesn't appear after creating
**Solution:**
1. Wait 2-3 seconds for refresh
2. Manually refresh page (F5)
3. Check browser console for errors
4. Verify backend is running

### Problem: Validation not working
**Solution:**
1. Refresh page (Ctrl+Shift+R for hard refresh)
2. Clear browser cache
3. Rebuild frontend: `npm run build`
4. Restart: `npm start`

### Problem: Seeing "CORS" error
**Solution:**
- This shouldn't happen but if it does:
- Check backend has CORS enabled
- Verify frontend and backend URLs are correct
- Check backend logs

---

## Success Checklist

After testing, verify:

- [ ] All 4 content items load on page refresh
- [ ] Can create new content through form
- [ ] Form validates required fields
- [ ] Form validates text length
- [ ] Form validates date (no past dates)
- [ ] Form validates numbers
- [ ] Can edit existing content
- [ ] Can delete content with confirmation
- [ ] Search filters content in real-time
- [ ] Can create categories
- [ ] Can edit categories
- [ ] Can delete categories
- [ ] Can send notifications
- [ ] Can delete notifications
- [ ] Error messages appear for API failures
- [ ] No red errors in browser console
- [ ] Network requests show 200/201 status codes

---

## Performance Notes

**Expected Response Times:**
- Loading content: 1-3 seconds
- Creating content: 1-2 seconds
- Updating content: 1-2 seconds
- Deleting content: 1-2 seconds
- Search filtering: Instant (no server call)
- Form validation: Instant (real-time)

If much slower, something may be wrong with the connection.

---

## Next Steps After Testing

If everything works:
1. ✅ Commit code to version control
2. ✅ Document any custom configurations
3. ✅ Plan deployment strategy
4. ✅ Set up authentication
5. ✅ Add more features as needed

If something fails:
1. ❌ Check the Troubleshooting section
2. ❌ Review browser console (F12)
3. ❌ Check backend logs
4. ❌ Verify API endpoints match
5. ❌ Restart both frontend and backend

---

**Ready to test? Start with Step 1!** 🚀
