FORMAT: 1A

ZeKKe API v0.1
==============

# Authorization API

Authorization in ZeKKe is built using JSON Web Token (JWT) standard.

Example:

```javascript
{
  "alg": "HS512",
  "typ": "JWT"
}
{
  "issuedAt": 1510361304550,
  "expirtation": 1520361304550,
  "issuer": "com.github.danzx.zekke",
  "subject": "ADMIN"
}
```

> There are currently 2 roles (_subject_ in token): **ADMIN** and **ANONYMOUS**

## Response structure

### Access token

| Field       | Optional | Type   | Description |
|-------------|----------|--------|-------------|
| accessToken | :o:      | string | JWT string  |

# Waypoint API

## Response structure

### Waypoint

A Waypoint is a generic representation of an arbitrary location that can be either a POI or a Walkway.

| Field    | Optional           | Type   | Description                                                    |
|----------|--------------------|--------|----------------------------------------------------------------|
| id       | :o:                | number | The id of this Waypoint                                        |
| type     | :o:                | string | Either _POI_ or _WALKWAY_                                      |
| name     | :heavy_check_mark: | string | The name or description of this Waypoint if it's of type _POI_ |
| location | :o:                | object | Where this Waypoint is located                                 |

### POI

A POI is a point of interest somewhere

| Field    | Optional           | Type   | Description                         |
|----------|--------------------|--------|-------------------------------------|
| id       | :o:                | number | The id of this POI                  |
| name     | :o:                | string | The name or description of this POI |
| location | :heavy_check_mark: | object | Where this POI is located           |

### Walkway

A walkway is arbitrary location with no relevant information but mapped in ZeKKe

| Field    | Optional | Type   | Description                   |
|----------|----------|--------|-------------------------------|
| id       | :o:      | number | The id of this Walkway        |
| location | :o:      | object | Where this Walkway is located |

#### Location

| Field     | Optional | Type   | Description                              |
|-----------|----------|--------|------------------------------------------|
| latitude  | :o:      | number | The latitude coordinate of the location  |
| longitude | :o:      | number | The longitude coordinate of the location |

# Endpoints

## Anonymous authentication [/api/v1/authentication/jwt/anonymous]

### Anonymous token generation [GET]

+ Request

    + Headers

            Accept: application/json

+ Response 200 (application/json)

        {
            "accessToken": "xxxxx.yyyyy.zzzzz"
        }

## Admin authentication [/api/v1/authentication/jwt/admin]

Requires authentication using user and password login set in the **Authorization** header as described in the HTTP Basic authentication standard

### Admin token generation [GET]

+ Request

    + Headers

            Authorization: Basic xxxxxxxxxxxx
            Accept: application/json

+ Response 200 (application/json)

        {
            "accessToken": "xxxxx.yyyyy.zzzzz"
        }

+ Response 401 (application/json)

        {
            "statusCode": 401,
            "errorDetail": "Unauthorized: Invalid user/password combination",
            "errorType": "AUTHORIZATION"
        }

## Waypoints collection [/api/v1/waypoints/{type}{?bbox,limit,query}]

+ Parameters

    + type: `pois` (optional, string) - Filter by _pois_ or _walkways_.
    + bbox: `12.23,32.681;15.234,37.65` (optional, string) - Finds all the waypoints within a rectangle specified by a latitude and logitude pair being the first the bottom left coordinates and the second the upper right coordinates.
    + limit: `1` (optional, number) - Limits the results to the given number.
    + query: `ara` (optional, string) - Filter by similar POI names. If this parameter is present, the paramater **type** will be ignored and use POI instead.


### List of waypoints [GET]

+ Request

    + Headers

            Authorization: Bearer xxxxx.yyyyy.zzzzz
            Accept: application/json

+ Response 200 (application/json)

        [
            {
                "id":1,
                "type":"WALKWAY",
                "location":
                {
                    "latitude":  19.387591,
                    "longitude": -99.052734
                }
            },
            {
                "id":2,
                "name":"A poi name",
                "type":"POI",
                "location":
                {
                    "latitude":  19.387591,
                    "longitude": -99.052734
                }
            }
        ]

+ Response 401 (application/json)

        {
            "statusCode": 401,
            "errorDetail": "Unauthorized",
            "errorType": "AUTHORIZATION"
        }

### Create a new waypoint [POST]

Requires **ADMIN** role

+ Request

    + Headers

            Authorization: Bearer xxxxx.yyyyy.zzzzz
            Accept: application/json
    + Body

            {
                "name":"My awesome POI",
                "type":"POI",
                "location":
                {
                    "latitude":  59.1331,
                    "longitude": -100.5012
                }
            }

+ Response 201 (application/json)

        {
            "id":3,
            "name":"My awesome POI",
            "type":"POI",
            "location":
            {
                "latitude":  59.1331,
                "longitude": -100.5012
            }
        }

+ Response 401 (application/json)

        {
            "statusCode": 401,
            "errorDetail": "Unauthorized",
            "errorType": "AUTHORIZATION"
        }
## POI name completition [/api/v1/waypoints/pois/names{?bbox,limit,query}]

+ Parameters
    + bbox: `12.23,32.681;15.234,37.65` (optional, string) - Finds all the waypoints within a rectangle specified by a latitude and logitude pair being the first the bottom left coordinates and the second the upper right coordinates.
    + limit: `1` (optional, number) - Limits the results to the given number.
    + query: `ara` (optional, string) - Filter by similar POI names. If this parameter is present, the paramater **type** will be ignored and use POI instead.

### List of tuples with POI id and name [GET]

+ Request

    + Headers

            Authorization: Bearer xxxxx.yyyyy.zzzzz
            Accept: application/json

+ Response 200 (application/json)

        [
            {
                "id":2,
                "name":"A poi name"
            },
            {
                "id":3,
                "name":"My awesome POI"
            }
        ]

+ Response 401 (application/json)

        {
            "statusCode": 401,
            "errorDetail": "Unauthorized",
            "errorType": "AUTHORIZATION"
        }

## Near waypoints [/api/v1/waypoints/{type}/near{?location,distance,limit}]

+ Parameters
    + type: `pois` (optional, string) - Filter by _pois_ or _walkways_.
    + location: `11.432,53.645`(required, string) - The center point to use.
    + distance: `500` (optional, number) - Limits the results to those waypoints that are at most the specified distance from the center point.
    + limit: `1` (optional, number) - Limits the results to the given number.

### List of waypoints [GET]

+ Request

    + Headers

            Authorization: Bearer xxxxx.yyyyy.zzzzz
            Accept: application/json

+ Response 200 (application/json)

        [
            {
                "id":1,
                "type":"WALKWAY",
                "location":
                {
                    "latitude":  19.387591,
                    "longitude": -99.052734
                }
            },
            {
                "id":2,
                "name":"A poi name",
                "type":"POI",
                "location":
                {
                    "latitude":  19.387591,
                    "longitude": -99.052734
                }
            }
        ]

+ Response 401 (application/json)

        {
            "statusCode": 401,
            "errorDetail": "Unauthorized",
            "errorType": "AUTHORIZATION"
        }

## Waypoint [/api/v1/waypoints/{id}]

Retrieve, update and remove a single waypoint object.

+ Parameters
    + id: `2` (required, number) - Numeric id of the waypoint to perform action with.

### Retrieve a waypoint [GET]

+ Request

    + Headers

            Authorization: Bearer xxxxx.yyyyy.zzzzz
            Accept: application/json

+ Response 200 (application/json)

        {
            "id":2,
            "name":"A poi name",
            "type":"POI",
            "location":
            {
                "latitude":  19.387591,
                "longitude": -99.052734
            }
        }

+ Response 401 (application/json)

        {
            "statusCode": 401,
            "errorDetail": "Unauthorized",
            "errorType": "AUTHORIZATION"
        }

+ Response 404 (application/json)

        {
            "statusCode": 404,
            "errorDetail": "Resource not found",
            "errorType": "NOT_FOUND"
        }

### Partially update a waypoint [PATCH]

Requires **ADMIN** role.

Use JSON Patch to specify which parts of the waypoint are going to be updated. See [JSON Pointer](https://tools.ietf.org/html/rfc6901) and [JSON Patch](https://tools.ietf.org/html/rfc6902)

+ Request

    + Headers

            Authorization: Bearer xxxxx.yyyyy.zzzzz
            Accept: application/json

    + Body

            [
                {
                    "op": "replace",
                    "path": "/name",
                    "value": "my new Awesome name"
                }
            ]

+ Response 200 (application/json)

        {
            "id":2,
            "name":"my new Awesome name",
            "type":"POI",
            "location":
            {
                "latitude":  19.387591,
                "longitude": -99.052734
            }
        }

+ Response 401 (application/json)

        {
            "statusCode": 401,
            "errorDetail": "Unauthorized",
            "errorType": "AUTHORIZATION"
        }

+ Response 404 (application/json)

        {
            "statusCode": 404,
            "errorDetail": "Resource not found",
            "errorType": "NOT_FOUND"
        }

### Remove a waypoint [DELETE]

Requires **ADMIN** role

+ Request

    + Headers

            Authorization: Bearer xxxxx.yyyyy.zzzzz
            Accept: application/json

+ Response 204

+ Response 401 (application/json)

        {
            "statusCode": 401,
            "errorDetail": "Unauthorized",
            "errorType": "AUTHORIZATION"
        }

+ Response 404 (application/json)

        {
            "statusCode": 404,
            "errorDetail": "Resource not found",
            "errorType": "NOT_FOUND"
        }
