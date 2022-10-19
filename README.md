# EDS-MOE
(E)electronic (D)ocument (S)ystem ver3.0

## Technologies used
- Java native (Backend)
- Javascript (next.js) (Frontend)

## Intellectual Property Rights
Deposit no. is 4123, deposited in ITIDA.GOV.EG.
غير مسموح بنشر أو توزيع أو تعديل نظام ادارة المستندات دون موافقة كتابية من المودع، طبقًا لقانون جمهورية مصر العربية رقم 82 لسنة 2002.

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

## Version 3.0 changelog
- Replaced SHA256 with SHA3-512
- Usernames and passwords will be hashed in storage
- configuration mode for Adding/Deleting/Changing passwords of users
- Fixed LOGJAM TLS Vulnerability
- Fixed Client Initiated Renegotiation TLS Vulnerability
- Bug Fixes
- Server key and user passwords should have an entropy 'e' where "e >= 55"
