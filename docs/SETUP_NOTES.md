# Setup Reference Notes

## Jenkins Plugins Required
- Maven Integration
- SonarQube Scanner
- Nexus Artifact Uploader
- Docker Pipeline
- Ansible
- Pipeline: Stage View
- Blue Ocean (optional, nicer UI)

## Jenkins Global Tool Configuration
- JDK-17 → point to JDK 17 install path
- Maven-3.9 → point to Maven 3.9 install path

## Jenkins Credentials to Add
| ID | Type | Used For |
|----|------|----------|
| nexus-credentials | Username/Password | Nexus upload + Docker registry login |
| sonarqube-token | Secret Text | SonarQube authentication |
| github-credentials | Username/Password or SSH key | Repo checkout |

## Jenkins Environment Variables (set globally in Manage Jenkins → System)
- NEXUS_HOST → private IP of Nexus server
- APP_SERVER_1 → private IP of App Server 1
- APP_SERVER_2 → private IP of App Server 2

## Nexus Repository Setup
1. Create a hosted Maven repository: `securebank-releases`
2. Create a Docker (hosted) repository: port 8082, enable "Allow anonymous docker pull" = NO
3. Add repository connector for Docker on a custom port (e.g., 8082)
4. Add `insecure-registries` entry on Jenkins server's Docker daemon.json pointing to Nexus:8082

## SonarQube Quality Gate (recommended starter rule)
- Coverage on new code < 80% → FAIL
- Duplicated lines on new code > 3% → FAIL
- Maintainability rating worse than A → FAIL

## PostgreSQL Setup Commands (run on DB server)
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib -y
sudo -u postgres psql -f database/01_create_database.sql
sudo -u postgres psql -d securebank -f database/02_schema.sql
sudo -u postgres psql -d securebank -f database/03_seed_data.sql
```

Also edit `/etc/postgresql/*/main/postgresql.conf`:
```
listen_addresses = '*'
```

And `/etc/postgresql/*/main/pg_hba.conf` — add:
```
host    securebank    securebank    10.0.3.0/24    md5
host    securebank    securebank    10.0.4.0/24    md5
```

Then `sudo systemctl restart postgresql`.

## Ansible Collections Needed
```bash
ansible-galaxy collection install community.docker
```

## Useful curl test commands (after deployment)
```bash
# Register
curl -X POST http://<APP_SERVER_IP>:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"Password123","email":"john@test.com"}'

# Login
curl -X POST http://<APP_SERVER_IP>:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"Password123"}'
```
