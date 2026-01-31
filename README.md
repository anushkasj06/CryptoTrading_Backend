🚀 Crypto Trading Platform
A full-stack Crypto Trading Platform built with Spring Boot, React.js, WebSockets, JWT Authentication, and PostgreSQL (NeonDB).
The platform enables users to securely trade crypto assets, participate in a real-time community chat, and manage accounts with role-based access.

🔗 Live Demo

🌐 Frontend: https://crypto-trading-frontend-new.vercel.app

⚙️ Backend: https://crypto-backend-foz1.onrender.com

🧩 Tech Stack

Frontend

React.js (Vite)

Redux Toolkit

Axios
Tailwind CSS + shadcn/ui
WebSocket (SockJS + STOMP)
Deployed on Vercel
Backend
Spring Boot
Spring Security + JWT Authentication
WebSocket (STOMP)
REST APIs
PostgreSQL (NeonDB – Serverless)
Dockerized Backend
Deployed on Render
Database
NeonDB (PostgreSQL – Free Tier)

✨ Features

🔐 Authentication & Security

JWT-based authentication
Role-based access control (USER / ADMIN)
Stateless session management
Secure password hashing (BCrypt)

📈 Trading Platform

Crypto asset listing
Buy & Sell crypto (simulation)
Wallet balance management
Transaction history

💬 Community Chat

Real-time chat using WebSockets
STOMP over SockJS
Message persistence
Online status indicators
Smooth UI with animations

⚙️ Admin Features

Admin-only endpoints
User and system management (extensible)

🔑 Environment Variables

Frontend (Vercel)
VITE_API_BASE_URL=https://crypto-backend-foz1.onrender.com

Backend (Render)
SPRING_DATASOURCE_URL=jdbc:postgresql:// SPRING_DATASOURCE_USERNAME= SPRING_DATASOURCE_PASSWORD= JWT_SECRET_KEY=

🧪 Running Locally 1️⃣ Clone the Repositories git clone git clone

2️⃣ Run Backend (Spring Boot) cd backend ./mvnw spring-boot:run

Backend will start on: http://localhost:5454

3️⃣ Run Frontend (React) cd frontend npm install npm run dev

Frontend will start on: http://localhost:5173

🔌 WebSocket Configuration

WebSocket Endpoint: /ws-community

Message Broker: /topic/community

Publish Destination: /app/chat

🔐 API Security Overview

Public Routes: /auth/** /ws-community/**

Protected Routes:

/api/**

Admin Routes:

/api/admin/**

Authorization handled via JWT in Authorization Header: Authorization: Bearer

