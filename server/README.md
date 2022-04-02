# Electronic Document System - Version 1.0

## Configuration
- Maximum Request Size : 50MB
- Maximum Concurrent Requests : 1000
- R.A.M Maximum usage before GC : 512MB
- Maximum Session IDs : 100
- Backlog Size : 10000

## Stages
- First Stage (main.java) : Check Frontend files
- Second Stage (main.java) : Database files and server key configuration or validation
- Third Stage (Engine.java) : Start HTTPS Server at port 443