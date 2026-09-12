# E-commerce API

A RESTful API for an e-commerce application with JWT authentication, email verification, product catalog, shopping cart, order checkout, product reviews, and role-based access control.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Security 6 + JWT
- PostgreSQL
- Docker + Docker Compose
- Swagger / OpenAPI
- JUnit 5 + Mockito

## Features

- User registration with email verification
- JWT authentication
- Role-based access control (USER, ADMIN)
- Product and category catalog with search by name and category
- Shopping cart (add/update/remove items) with stock validation
- Order checkout from cart, order cancellation
- Product reviews (create/update/delete, view by product)
- Admin panel (manage users, block/unblock users, manage reviews)
- User can update and delete their own account

## Getting Started

### Prerequisites

- Docker + Docker Compose

### Run with Docker

1. Clone the repository:
```bash
   git clone https://github.com/rskoryk/e-commerce.git
   cd e-commerce
```
2. Create `.env` file from the example:
```bash
   cp .env.example .env
```
3. Fill in the `.env` file with your values:
```bash
   JWT_SECRET=your_base64_secret
   MAIL_USERNAME=your_mailtrap_username
   MAIL_PASSWORD=your_mailtrap_password
```
4. Run the application:
```bash
   docker-compose up --build
```
5. Open Swagger UI:
```bash
   http://localhost:8080/swagger-ui/index.html
```

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register a new user |
| POST | /api/auth/login | Login and get JWT token |
| GET | /api/auth/verify | Verify email |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/products | Get all products |
| GET | /api/products/search/by-name | Search products by partial name |
| GET | /api/products/search/by-category | Get products by category |
| POST | /api/products | Create a product (ADMIN) |
| PUT | /api/products/{id} | Update a product (ADMIN) |
| DELETE | /api/products/{id} | Delete a product (ADMIN) |

### Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/categories | Get all categories |
| GET | /api/categories/{id} | Get category by ID |
| GET | /api/categories/search | Search categories by partial name |
| POST | /api/categories | Create a category (ADMIN) |
| PUT | /api/categories/{id} | Update a category (ADMIN) |
| DELETE | /api/categories/{id} | Delete a category (ADMIN) |

### Cart
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/cart | Get current user's cart |
| POST | /api/cart/items | Add item to cart |
| PUT | /api/cart/items/{id} | Update item quantity |
| DELETE | /api/cart/items/{id} | Remove item from cart |

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/orders/checkout | Place an order from the cart |
| GET | /api/orders | Get current user's orders |
| GET | /api/orders/{id} | Get order by ID |
| DELETE | /api/orders/{id} | Cancel an order |

### Reviews
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/reviews | Create a review |
| GET | /api/reviews/product/{productId} | Get reviews for a product |
| PUT | /api/reviews/{id} | Update own review |
| DELETE | /api/reviews/{id} | Delete own review |

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/users/me | Get current user |
| PUT | /api/users/me | Update current user |
| DELETE | /api/users/me | Delete current user |

### Admin
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/admin/users | Get all users |
| GET | /api/admin/users/{id} | Get user by ID |
| PUT | /api/admin/users/{id}/block | Block a user |
| PUT | /api/admin/users/{id}/unblock | Unblock a user |
| PUT | /api/admin/reviews/{id}/deactivate | Hide a review |
| PUT | /api/admin/reviews/{id}/activate | Restore a review |
| DELETE | /api/admin/reviews/{id} | Permanently delete a review |

## Running Tests
```bash
  ./mvnw test
```

## Environment Variables
| Variable | Description |
|----------|-------------|
| JWT_SECRET | Base64 encoded secret key (min 256 bit) |
| MAIL_USERNAME | Mailtrap SMTP username |
| MAIL_PASSWORD | Mailtrap SMTP password |
