# Priority Queue Service
The Priority Queue Service is a RESTful microservice which allows the management of work orders. The service provides APIs for adding 
The service provides the following functionality
* Add work orders to the queue
* Pop work orders off the queue
* Query specific work orders
* Query the list of open work orders
* Removal of work orders
* Query the average wait time of orders in the queue
* Query the 95th percentile of wait time of orders in the queue

There are 4 types of priorities used by the system. Priorities are specified through the ID of the user.
* Normal
* Priority (evenly divisible by 3)
* VIP   (evenly divisible by 5)
* Management Override (evenly divisible by 3 and 5)

# Build the project and run tests
```
mvn verify
```

# Deploy the service
```
mvn spring-boot:run
```

# Postman Suite
A Postman suite containing all APIs provided by the service is included in this project. 
To import this into Postman, click on the **Import** button on the top left corner of Postman.
Each API can then be run against the running service by clicking on the request in the left hand panel.
Then click on the **Send** button on the main panel in Postman. Notice that some APIs require some modification 
with real data before a request is valid and accepted by the service.

# Queue Service API Definition

# Add to queue
## Description
Add an ID to the queue

### URL
http://(baseurl)/queue

### URL Parameters
None

### Method
POST

### Request Headers

| Header name | Description |
| --- | --- |
| Content-Type | Content type of the request. application/json is the only supported content type |

### Request Body
```
{
	"userId": <Long>,
	"date": <date>
}
```

| Parameter name | Description |
| --- | --- |
| userId | Must be a number between 1 - 9223372036854775807 |
| date | Must be a string in the format of yyyy-MM-dd-HH-mm-ss |

### Response Body
```
{
    "userId": <Long>,
    "date": <date>
}
```

### Response Codes

| Status Code | Description |
|---------- |----------|
| 200       | Request completed successfully |
| 400       | Request is invalid |
| 409       | Could not complete request, ID already submitted for user |
| 500 | An internal error was encountered by the service |
| 503 | Request could not be completed as the service is unavailable |



# Get top ID from the queue
## Description
Get the top ID from the queue. This pulls from the top of the queue and removes it from the queue.

### URL
http://(baseurl)/queue/top

### URL Parameters
None

### Method
GET

### Request Body
None

### Response Body
```
{
	"userId": <Long>,
	"date": <date>
}
```

### Response Codes

| Status Code | Description |
|---------- |----------|
| 200       | Request completed successfully |
| 500 | An internal error was encountered by the service |
| 503 | Request could not be completed as the service is unavailable |


# Get all IDs from the queue
## Description
Get a list of all the IDs in the queue, this is sorted from highest rank to lowest

### URL
http://(baseurl)/queue

### Method
GET

### Request Headers
None required

### Request Body
None

### Response Body
```
{
	"allIds": [<IDs>]
}
```

### Response Codes
| Status Code | Description |
|---------- |----------|
| 200       | Request completed successfully |
| 500 | An internal error was encountered by the service |
| 503 | Request could not be completed as the service is unavailable |


# Remove ID from the queue
## Description
Remove a specific ID from the queue

### URL
http://(baseurl)/queue/\<id\>

### Method
DELETE

### URL Parameters
| Parameter name | Description |
| --- | --- |
| id    | ID to be deleted |

### Request Headers

| Header name | Description |
| --- | --- |
| Content-Type | Content type of the request. application/json is the only supported content type |

### Request Body
None

### Response Body
None

### Response Codes
| Status Code | Description |
|---------- |----------|
| 204       | Request completed successfully. |
| 500 | An internal error was encountered by the service |
| 503 | Request could not be completed as the service is unavailable |




# Get the position of an ID in the queue
## Description
Get the position of an ID in the queue. Note that positions start at 0.


### URL
http://(baseurl)/queue/\<id\>

### URL Parameters
| Parameter name | Description |
| --- | --- |
| id    | ID to be deleted |

### Method
GET

### Request Headers

| Header name | Description |
| --- | --- |
| Content-Type | Content type of the request. application/json is the only supported content type |

### Request Body
None

### Response Body
```
{
    "position": <Long>
}
```

### Response Codes
| Status Code | Description |
|---------- |----------|
| 200       | Request completed successfully. |
| 404 | Resource not found |
| 500 | An internal error was encountered by the service |
| 503 | Request could not be completed as the service is unavailable |


# Get the average wait time
## Description
Get the average wait time for each request in the queue.


### URL
http://(baseurl)/queue/avg-wait-time/{from-date}

### URL Parameters
None

### Method
GET

### Request Headers
None

### Request Body
None

### Response Body
```
{
    "averageWaitTime": <Long>
}
```

### Response Codes
| Status Code | Description |
|---------- |----------|
| 200       | Request completed successfully |
| 400 | Request is invalid |
| 500 | An internal error was encountered by the service |
| 503 | Request could not be completed as the service is unavailable |


# Get the 95th percentile wait time
## Description
An endpoint to get the 95th percentile wait time of all IDs over time. 
This endpoint returns the 95th percentile of number of seconds IDs have been waiting 
in the queue since the application started.

### URL
http://(baseurl)/queue/percentile95

### URL Parameters
None

### Method
GET

### Request Headers
None

### Request Body
None

### Response Body
```
{
    "percentile95": <Long>
}
```

### Response Codes
| Status Code | Description |
|---------- |----------|
| 200       | Request completed successfully |
| 500 | An internal error was encountered by the service |
| 503 | Request could not be completed as the service is unavailable |