# SecureBank - Account Encryption System

A small Java Swing + MySQL Information Security student project.

## Features
- AES-256-GCM encryption of account numbers before database storage.
- Decryption when a record is retrieved.
- SHA-256 digest check over holder name, account number and balance.
- MySQL storage through JDBC.
- One-screen Swing interface.

## Requirements
- JDK 17 or newer (the DAO uses Java text blocks).
- MySQL Server.
- MySQL Connector/J.
- IntelliJ IDEA (Maven support is built in).

## Setup (IntelliJ IDEA)
1. Run `database/securebank.sql` in MySQL Workbench.
2. Open `src/config/DBConnection.java` and set your MySQL password.
3. In IntelliJ: File > Open > select the `SecureBank` folder (the one with `pom.xml`) > Open as Project.
   Maven downloads the MySQL driver automatically. If a popup appears, click "Load Maven Changes".
4. Run `src/main/Main.java`.

Without Maven: download Connector/J JAR, then File > Project Structure > Libraries > + > Java > select the JAR > OK.

## Security note
The built-in encryption key is for a classroom demonstration only. For a better setup,
set `SECUREBANK_KEY_BASE64` to a Base64-encoded, randomly generated 32-byte key before
launching the app. Keep the same key for future runs; changing it makes old records
undecryptable. Never use this demo as a real banking system.

The SHA-256 digest is an educational integrity demonstration, not a keyed tamper-proof
authentication mechanism. In a production system use a keyed MAC (such as HMAC) and
proper key management.
