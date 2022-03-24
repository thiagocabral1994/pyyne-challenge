Hello!

To run this submission, please follow bellow instructions:

#### Prerequisite 

Installed:   
[Docker](https://www.docker.com/)   
[git](https://www.digitalocean.com/community/tutorials/how-to-contribute-to-open-source-getting-started-with-git)   

Optional:   
[Docker-Compose](https://docs.docker.com/compose/install/)   
[Java 11+](https://www.oracle.com/technetwork/java/javase/overview/index.html)   
[Maven 3.x](https://maven.apache.org/install.html)

#### Steps

##### Clone source code from git
```
$  git clone https://github.com/thiagocabral1994/pyyne-challenge.
```

## Run with docker-compose 

Build and start the container by running

```
$ docker-compose up -d --build 
```

##### Test application with ***curl*** command

Getting balance summary:
```
$ curl localhost:8080/bank/balances/1
```

the respone should be:
```json
{
  "balances": {
    "bank2": {
      "balance": 512.5,
      "currency": "USD"
    },
    "bank1": {
      "balance": 215.5,
      "currency": "USD"
    }
  }
}
```

Getting transactions summary:
```
$ curl http://localhost:8080/bank/transactions/1?from=2013-09-10T20%3A00%3A00Z&to=2013-09-10T20%3A00%3A00Z
```

the respone should be:
```json
{
  "transactions": {
    "bank2": [
      {
        "amount": 125.0,
        "type": "DEBIT",
        "text": "Amazon.com"
      },
      {
        "amount": 500.0,
        "type": "DEBIT",
        "text": "Car insurance"
      },
      {
        "amount": 800.0,
        "type": "CREDIT",
        "text": "Salary"
      }
    ],
    "bank1": [
      {
        "amount": 100.0,
        "type": "CREDIT",
        "text": "Check deposit"
      },
      {
        "amount": 25.5,
        "type": "DEBIT",
        "text": "Debit card purchase"
      },
      {
        "amount": 225.0,
        "type": "DEBIT",
        "text": "Rent payment"
      }
    ]
  }
}
```

##### Stop Docker Container:
```
docker-compose down
```
