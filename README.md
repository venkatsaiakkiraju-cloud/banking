# SecureBank Digital Banking Platform

A production-style banking platform built across 8 Spring Boot microservices, designed as an
AWS DevOps capstone project. **This zip contains 100% of the application code.** You will perform
all the infrastructure, CI/CD, and operations work yourself, guided step-by-step.

---

## 📂 What's in this zip

```
securebank-platform/
├── auth-service/            # JWT login, registration, role management (port 8081)
├── customer-service/        # Customer profile + KYC status (port 8082)
├── account-service/         # Savings/Current accounts, balance ops (port 8083)
├── transaction-service/     # Fund transfer, history, mini statement (port 8084)
├── loan-service/            # Loan application, EMI calc, approve/reject (port 8085)
├── employee-service/        # KYC verification, account freeze (port 8086)
├── notification-service/    # Email/SMS notification log (port 8087)
├── audit-service/           # Audit trail for compliance (port 8088)
├── database/                # PostgreSQL schema + seed data SQL scripts
├── deployment/
│   ├── ansible/              # Playbooks: docker install, deploy, rollback, hardening
│   ├── monitoring/           # Prometheus + Grafana configs
│   └── elk/                  # ELK stack (Elasticsearch, Logstash, Kibana)
├── docker-compose.yml        # Run the ENTIRE platform locally with one command
└── securebank-platform.code-workspace   # Open this file in VS Code
```

Every microservice already includes:
- ✅ Full Java source (Controller → Service → Repository → Entity)
- ✅ `pom.xml` (Maven build file)
- ✅ `application.properties`
- ✅ `Dockerfile`
- ✅ `Jenkinsfile` (full CI/CD pipeline: build → test → SonarQube → Nexus → Docker → Ansible deploy → health check)
- ✅ A basic JUnit context-load test (using H2 in-memory DB)

---

## 🚀 Quick Start — Open in VS Code

1. Unzip the file anywhere on your PC
2. Open VS Code
3. File → Open Workspace from File → select `securebank-platform.code-workspace`
4. Install the recommended extensions when prompted (Java Extension Pack, Docker, Ansible, etc.)

---

## 🧪 Test Everything Locally FIRST (before touching AWS)

You should always verify the app works before deploying it — this is what real DevOps engineers do.

```bash
cd securebank-platform
docker-compose up -d --build
```

This builds and starts:
- PostgreSQL (with schema + seed data auto-loaded)
- All 8 microservices
- Prometheus (http://localhost:9090)
- Grafana (http://localhost:3000 — login: admin / admin123)

Check everything is healthy:
```bash
curl http://localhost:8081/actuator/health   # auth-service
curl http://localhost:8082/actuator/health   # customer-service
curl http://localhost:8083/actuator/health   # account-service
curl http://localhost:8084/actuator/health   # transaction-service
curl http://localhost:8085/actuator/health   # loan-service
curl http://localhost:8086/actuator/health   # employee-service
curl http://localhost:8087/actuator/health   # notification-service
curl http://localhost:8088/actuator/health   # audit-service
```

Test the registration + login flow:
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Password123","email":"test@test.com"}'

curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Password123"}'
```

When you're confident it works locally, **stop the local stack** before deploying to AWS:
```bash
docker-compose down
```

---

## 🗺️ Your DevOps Journey (in order)

| # | Phase | What You Do |
|---|-------|-------------|
| 1 | **AWS Infrastructure** | Create VPC, subnets, EC2s, security groups |
| 2 | **Linux Prep** | Connect via Bastion, harden servers |
| 3 | **GitHub Setup** | Push this code to your own repo |
| 4 | **PostgreSQL** | Install on DB server, run `database/` scripts |
| 5 | **Dockerization** | Build images, run containers on App Servers |
| 6 | **Jenkins** | Install, configure pipelines using provided Jenkinsfiles |
| 7 | **SonarQube** | Install, connect to Jenkins, set quality gates |
| 8 | **Nexus** | Install, create repositories for JARs + Docker images |
| 9 | **Ansible** | Use provided playbooks for automated deployment |
| 10 | **Prometheus + Grafana** | Use provided configs, build dashboards |
| 11 | **ELK Stack** | Centralize logs from all services |
| 12 | **Security Hardening** | Run the hardening playbook, set up IAM least privilege |
| 13 | **Failure Simulations** | Intentionally break things, observe alerts, recover |
| 14 | **Documentation** | Architecture diagrams, runbooks |

We'll go through each phase together, one at a time, hands-on.

---

## 🔑 Default Test Credentials (after seeding database)

| Username | Password | Role |
|----------|----------|------|
| admin | Password123 | ROLE_ADMIN |
| employee1 | Password123 | ROLE_EMPLOYEE |
| customer1 | Password123 | ROLE_CUSTOMER |

---

## ⚙️ Service Communication Map

```
Customer/Employee/Admin
        ↓
   Auth Service (8081) ──validates JWT for all services
        ↓
┌───────┴────────┬─────────────┬──────────────┬───────────┐
Customer(8082) Account(8083) Transaction(8084) Loan(8085) Employee(8086)
                                    ↓                          ↓
                            calls Account Service      calls Customer/Account/Loan
                                                                ↓
                                                        Notification(8087)
                                                        Audit(8088) — logs everything
```

---

## 📝 Notes on the Code

- **JWT secret** is shared via env var `JWT_SECRET` across services — in production this would live in AWS Secrets Manager / Parameter Store (you'll set this up in the Security Hardening phase)
- **Inter-service calls** use `RestTemplate` (synchronous HTTP) — this mirrors what's described in the original project brief
- **Optimistic locking** (`@Version`) is used on the Account entity to prevent race conditions during concurrent balance updates
- **EMI calculation** in loan-service uses the standard reducing-balance formula
- All services expose `/actuator/health` and `/actuator/prometheus` for monitoring

---

Ready? Let's start with **Phase 1: AWS Infrastructure** whenever you are.
