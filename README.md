# EDS-MOE
(E)electronic (D)ocument (S)ystem ver2.2

## Technologies used
- Java native (Backend)
- Javascript (next.js) (Frontend)

## Intellectual Property Rights
Deposit no. is 4123, deposited in ITIDA.GOV.EG.

## Details
- The max. number of documents can be inserted is 10mil. with FIFO system
- The system doesn't allow update or delete queries
- EDS uses TOTP
- EDS uses AES256/CBC/PKCS5 crypt algorithm
- 'prod/' is a folder for latest production
- Max Request size is 10MB
- Max concurrent requests is 5000
- Max backlog is 50000
- Max session IDs is 100 with FIFO system
- If max conc. requests is reached, retry 10 times with 1ms delay to process the query.
- BUF_SIZE for RCV and SNT is 64KB
- SO_TIMEOUT is 60sec

## TODO
- [ ] Config terminal mode for :<br>
						1. CRUD in './conf/users.db'
						2. Update school name