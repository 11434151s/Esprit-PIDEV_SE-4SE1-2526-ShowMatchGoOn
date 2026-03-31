# Frontend Test Organization

## Test Structure by Feature

This directory organizes all frontend unit tests by feature/module. Each feature has its own subdirectory containing component and service tests.

### Directory Layout

```
__tests__/
├── auth/              # Authentication feature
│   ├── auth.service.spec.ts           (4 tests)
│   └── login.component.spec.ts        (3 tests)
│
├── admin/             # Admin dashboard features
│   ├── admin-users.component.spec.ts  (4 tests)
│   └── admin-content.component.spec.ts (1 test)
│
└── app/               # Application root component
    └── app.component.spec.ts          (1 test)
```

## Test Summary

| Feature | Component | Service | Total |
|---------|-----------|---------|-------|
| Auth | 3 | 4 | 7 |
| Admin | 5 | - | 5 |
| App | 1 | - | 1 |
| **TOTAL** | **9** | **4** | **13** |

## What Each Test Type Covers

### Component Tests
- Component creation and initialization
- User interaction (form submission, button clicks)
- Template rendering
- Integration with services
- Route/navigation handling

### Service Tests
- HTTP request/response handling
- Data transformation
- Authentication token management
- Error handling
- Local storage operations

## Running Tests

### Run all tests
```bash
ng test --watch=false
```

### Watch mode (re-run on file changes)
```bash
ng test
```

### Run tests for a specific feature
Tests are organized by directory and can be run by feature. Look for spec files in each directory.

## Test Organization Principles

1. **One feature per directory** - Each feature has its own folder
2. **Component + Service together** - Related test files live in the same directory
3. **Lean tests** - Minimal tests covering essential functionality
4. **User-focused** - Tests verify user interactions and workflows
5. **Service isolation** - Services are mocked to test components in isolation
6. **No HTTP calls** - All HTTP is mocked using HttpTestingController

## Adding New Tests

When adding tests for a new feature:
1. Create a new directory under `__tests__/` (e.g., `reviews/`)
2. Add `xyz.component.spec.ts` for component tests
3. Add `xyz.service.spec.ts` for service tests (if applicable)
4. Mock external dependencies (HttpClient, Router, etc.)
5. Update this README with the new feature
6. Keep tests focused and avoid duplication

## Test Environment

- **Test Runner**: Karma 6.4.4
- **Test Framework**: Jasmine 5.2.0
- **Browser**: Opera GX (Chromium-based)
- **HTTP Mocking**: HttpTestingController
- **Service Mocking**: jasmine.createSpyObj()

## Configuration Files

- `karma.conf.js` - Karma test runner configuration
- `angular.json` - Angular testing configuration
- `.spec.ts` files - Individual test suites
