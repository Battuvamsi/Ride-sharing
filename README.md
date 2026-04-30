# Employee Ride Sharing (Spring Boot + React)

A full-stack employee ride-sharing app with gender-based rider selection and ride matching.

## Backend (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

Backend API: `http://localhost:8080/api`

### API Endpoints

- `POST /api/employees`
- `GET /api/employees`
- `POST /api/rides`
- `GET /api/rides`
- `GET /api/matches?minScore=40`
- `GET /api/analytics`

## Frontend (React + Vite)

```bash
cd frontend
npm install
npm run dev
```

Frontend: `http://localhost:5173`

## Matching + Gender Selection

Supported gender preference values for rides:

- `ANY`
- `FEMALE_ONLY`
- `MALE_ONLY`
- `SAME_GENDER`

Match scoring uses:
- preference compatibility
- departure time proximity
- pickup/dropoff similarity
- ride type
- smoking preference

## Tests

```bash
cd backend
mvn test
```
