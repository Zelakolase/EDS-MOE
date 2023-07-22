# Electronic Document System
A highly secure and simple document management system, written in Java. Every document is unmodifiable, and issued a 9-digit code.

- [Electronic Document System](#electronic-document-system)
  - [Use cases](#use-cases)
  - [Example Usage](#example-usage)
  - [Build and Run](#build-and-run)
  - [Technologies used](#technologies-used)
  - [Legal Information](#legal-information)
  - [Security Measures](#security-measures)
  - [Backup and Restore](#backup-and-restore)
  - [Class List](#class-list)
  - [Author Information](#author-information)


## Use cases
1. Replace the official government seal with a 9-digit code acting as a digital signature.
2. Store documents and share them in a highly secured system across a network.
3. Integrating EDS API in a larger high-security system for document archival and secure storage.

## Example Usage
1. A verifier inserts a document through a 2-step process in 'Submit A document' tab.
2. The system generates a 9-digit code for the inserted document and displays it to the verifier.
3. Any member of the public can download the document, view information about who wrote it and the date of publication, or compare it with another file using the 9-digit code.
4. The verifier can access the list of documents and their 9-digit codes later in the 'Board' tab.

## Build and Run
The main instance of the application is `main.java`.
- In order to run it in linux systems: `$ javac main.java && sudo java main`
- In order to run it in windows systems: `> javac -encoding iso8859_1 main.java && java main`

## Technologies used
- Backend: Java Native
- Frontend: NextJS

## Legal Information
This software is an intellectual property in all of the WIPO member states (Egypt Deposit number: 4123). The repository inherits all of its legal statements from the Egyptian Intellectual Property Protection Law No. 82 of 2002.

## Security Measures
1. We do not use common architecture (eg. Apache, nginx, SQL-based solutions). Instead, we built the backend from scratch. This means that EDS will not be hit by any CVE attack.
2. Verifiers' credentials are encrypted by AES256/CBC/PKCS5 algorithm and hashed with SHA3-512.
3. All databases, users' credentials, configurations, and Frontend files are encrypted to limit the impact of physical attacks.
4. SO_Timeout is limited to 60 seconds.
5. We implemented Time-based One time passwords for verifiers' authentication.
6. The maximum number of documents that can exist in the system at any given time is 10 Million documents with FIFO insertion system.

## Backup and Restore
To move your data in another system, just copy data/ folder in server/src/ and paste the folder in server/src/ of the new system. Note that older versions (< 4.0) would need to group conf/ and docs/ into a new folder data/ to migrate to version (>= 4.0).

## Class List
- www/
    - Content is generated using GitHub Actions.
- Endpoints/
    - **about.java** : Handles 'api.about' response.
    - **DataDoc.java** : Handles 'api.DataDoc' response.
    - **DownloadDoc.java** : Handles 'api.DownloadDoc' response.
    - **generate.java** : Handles 'api.generate' response.
    - **login.java** : Handles 'api.login' response.
    - **logout.java** : Handles 'api.logout' response.
    - **name.java** : Handles 'api.name' response.
    - **SearchDoc.java** : Handles 'api.SearchDoc' response.
    - **Table.java** : Handles 'api.table' response.
    - **VerifyDoc.java** : Handles 'api.VerifyDoc' response.
- lib/
    - **AES.java** : AES256/CBC/PKCS5 Encryption and Decryption Class.
    - **ArraySplit.java** : Splits a byte array based on a specific byte sequence as a delimiter.
    - **EntropyCalc.java** : Calculation of entropy for a given passphrase.
    - **FileToAL.java** : Converts a file content to an arraylist of lines based on '\n' delimiter, used for 'WWWFiles.db'.
    - **HeaderToHashmap.java** : Converts the HTTP(S) Headers to a HashMap structure.
    - **HTTPCode.java** : An enum of String values to map HTTP(S) statuses to their corresponding header with the status code.
    - **FilePaths.java** : An enum of all configuration and database directories.
    - **IO.java** : Reads and Writes data to Disk.
    - **JSON.java** : Converts JSON String to a HashMap structure, and vice versa.
    - **log.java** : Prints different log statements with colors.
    - **MaxSizeHashMap.java** : A HashMap with a fixed size where it deletes the eldest entry if the HashMap is full.
    - **MemMonitor.java** : A thread object that calls Garbage Collection every 50ms if the memory usage went above 1GB.
    - **Network.java** : Handles GZIP Compression, read from TCP socket and write HTTP(S) responses to TCP socket.
    - **PathFilter.java** : Provides LFI Protection and handles different path typos.
    - **PostRequestMerge.java** : Reads the whole HTTPS POST request beyond TLS record limitation (16kb).
    - **RandomGenerator.java** : Generates Random String values.
    - **SHA.java** : SHA3-512 UTF8 Hashing Class.
    - **SparkDB.java** : CSV to in-memory structure, see the [main project](https://github.com/NaDeSys/SparkDB) for more information.
    - **TOTP.java** : Handles Time-based One Time Passwords generation and validation algorithms.
    - **URIDecode.java** : Decodes URI (HTML) format.
    - **CommandPrompt.java** : An abstract Command Prompt Object.
- **API.java** : The intermediate point that maps API requests from the main HTTPS instance to individual API Classes.
- **Engine.java** : The main HTTPS Instance, called by main application instance 'main.java'.
- **main.java** : The main Application instance, configures the server if it is first-time run, or calls the HTTPS Instance 'Engine.java' if the inserted Server Key is correct.
- **Server.java** : TCP HTTPS Handler Class, used by main HTTPS Instance 'Engine.java'.
- **mime.db** : A list of MIME types and their corresponding file extensions.
- **WWWFiles.db** : A white-list of files in www/ to access via HTTPS protocol.
- **ConfigMode.java** : The configuration mode instance, called by main application instance 'main.java' if the user requested the configuration mode.

## Author Information
- Morad Abdelrasheed Mokhtar Ali Gill (Zelakolase@tuta.io)
- Mohammed Emad Mohamed Ahmed Elsawy (hulxxv@gmail.com)
- Mustafa Anwar Hamza Selim (manwar.hamza@gmail.com)
- Andrew Roshdy Morad Yousef (andrewroshdydodo28@gmail.com)