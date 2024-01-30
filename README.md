# wolpha-calculator
 A simple Wolfram Alpha alternative

 (Trigger warning: Jank implementation)

## Usage

To use it, submit POST requests to the endpoint with the body as an expression you want to calculate.

Example: `curl -d "420+69" -H "Content-Type: text/plain" -X POST https://wolpha-calculator.herokuapp.com/` would return 489.0.

## Features

1. Arbitrary input precision, up to 10 decimal digits of output precision.
2. Elementary operations: `+`, `-`, `*` or `x`, `/`, `^`
3. Elementary functions: `sin`, `cos`, `tan`, `sqrt`, `ln` or `log`
4. Parenthesis and order of operations (PEMDAS)
5. Constants: `e` and `pi`
6. Decimals and negative values (e.g., -1.5)

## Example

1. `1+1` returns 2.0
2. `5-(-1.9)` returns 6.9
3. `2+2*2` returns 6.0 (by PEMDAS)
4. `2^2^3` returns 256.0 (evaluate right to left for exponents)
5. `sin(cos(pi/2))` returns 0.0
6. `1.99999999999999999999` returns 2.0
7. `error` returns HTTP 400

## Setup 

(The rest of this file is only for if you want to host the repository separately.)

## Local Setup

1. Clone this repository.
2. Install and initialize PostgreSQL.
3. Run the program with specified environment variables.

### Environment Variables

1. `DB_HOST`: Host name (e.g., localhost)
2. `DB_PORT`: PostgreSQL port (e.g., 5432)
3. `DB_USER`: Username (e.g., jean)
4. `DB_PASS`: Password (e.g., password)
5. `DB_NAME`: Database (e.g., jean)

## Heroku Setup

1. Create a Heroku app, then push the repository.
2. View database credentials at App -> Resources -> Heroku Postgres -> Settings -> View Credentials...
3. To access the database, use `heroku pg:psql`.
4. View the table using `SELECT * FROM entity`.
