# EDS-MOE
(E)electronic (D)ocument (S)ystem ver3.2. A fast and secure document management system to replace government seals!

## Technologies used
- Java native (Backend)
- Javascript (next.js) (Frontend)

## Intellectual Property Rights
Deposit no. is 4123, deposited in ITIDA.GOV.EG.

## Features
- Time-based One time password for verifiers' accounts
- A configuration mode for changing passwords, adding or deleting verifiers into the system
- Download, Verify, and search for any document using a numeric 9 digit code for every document
- Verifiers' credentials is encrypted by AES256/CBC/PKCS5 algorithm and hashed with SHA3-512
- All files (including front-end files) are encrypted to limit Physical attacks
- From-scratch solutions to evade any web pentesting tools, we do **not** use Apache, nginx, or SQL-based solutions

## Details
- The max. number of documents can be inserted is 10mil. with FIFO system
- The system doesn't allow update or delete queries
- EDS uses TOTP
- EDS uses AES256/CBC/PKCS5 crypt algorithm
- 'prod/' is a folder for latest production (is not in repository)
- Max Request size is 10MB
- Max concurrent requests is 5000
- Max backlog is 50000
- Max session IDs is 100 with FIFO system
- If max conc. requests is reached, retry 10 times with 1ms delay to process the query.
- BUF_SIZE for RCV and SNT is 64KB
- SO_TIMEOUT is 60sec

## Authors (and Contact E-mails)
- Morad Abdelrasheed Mokhtar Ali Gill (Zelakolase@tuta.io)
- Mohammed Emad Mohamed Ahmed Elsawy (hulxxv@gmail.com)
- Mustafa Anwar Hamza Selim (manwar.hamza@gmail.com)
- Andrew Roshdy Morad Yousef (andrewroshdydodo28@gmail.com)

## How you can support us?
- You can write about us, for more information about this project, please contact (Zelakolase@tuta.io).
- Star this project.
- Actively implement this project in your workplace!