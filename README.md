# CardProcessorService
### Validate Card Data and other related Card functionality



Using the information provided below (in the table) as a guide, write a program that will process the
data based on the criteria provided below:

1. Validate the card data entry
2. Sort the data by expiry date into descending order
3. Obfuscate the card number, except the last four digits, with ‘x’. 
e.g. 5601-2345-3446-5678 becomes xxxx-xxxx-xxxx-5678

Bank details   | Bank Card number   | Expiry date
- HSBC Canada,5601-2345-3446-5678,Nov-2017
- Royal Bank of Canada,4519-4532-4524-2456,Oct-2017
- American Express,3786-7334-8965-345,Dec-2018

You should wrap this code into a stand-alone web application which can be deployed / run in the
following way:

- Directly from Maven using Spring Boot
- We will be using Java 9+ to run the code.

The end user should be able to enter the card data manually, one row at a time, or upload a simple
CSV file with the columns in the order shown above. Entered data need only be stored for the duration
of the user session and does not need database persistence.

The results should be presented on a web page.

### API ENDPOINTS

Available endpoings are as follows:

- GET /api/card/test - For Connection testing
- GET /api/card/      - For getting all stored Card objects
- POST /api/card/      - Post a single Card into the system
- POST /api/card/uploadCsv  - Upload an appropriately formatted CSV file to upload multiple Cards at once.
