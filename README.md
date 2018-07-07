# Build the project and run tests
```
mvn verify
```

# Deploy the service
```
mvn spring-boot:run
```

# Queue Service API Definition

# Add to queue
## Description
Add an ID to the queue

### URL
http://(baseurl)/api/v1/queue

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
    "date": <date>,
    "rank": <Long>
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
http://(baseurl)/api/v1/queue/top

### URL Parameters
None

### Method
GET

### Request Body
None

### Response Body
```
{
	"rank": <Long>,
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
http://(baseurl)/api/v1/queue

### Method
GET

### Request Headers
None required

### Request Body
None

### Response Body
```
{
	"ids": [<IDs>]
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
http://(baseurl)/api/v1/queue/\<id\>

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
Get the position of an ID in the queue


### URL
http://(baseurl)/api/v1/queue/\<id\>

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
http://(baseurl)/api/v1/queue/avg-wait-time

### URL Parameters
None

### Method
GET

### Request Headers
None

### Request Body
```
{
    "fromTime": <Date>
}
```

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
An endpoint to get the 95th percentile wait time of all IDs over time. This endpoint should return the 95th percentile of number of seconds IDs have been waiting in the queue since the application started.

### URL


### URL Parameters

### Method

### Request Headers

### Request Body
```

```

### Response Body
```

```

### Response Codes
