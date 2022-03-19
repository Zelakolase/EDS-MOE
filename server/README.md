# Electronic Document System - Version 1.0

## Configuration
- Maximum Request Size : 100MB
- Maximum Concurrent Requests : 2500
- R.A.M Maximum usage before GC : 512MB
- Maximum Session IDs : 100

## Stages
- First Stage (main.java) : Check Frontend files
- Second Stage (main.java) : Database files and server key configuration or validation
- Third Stage (Engine.java) : Start HTTPS Server at port 443