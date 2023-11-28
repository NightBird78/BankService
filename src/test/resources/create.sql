CREATE TABLE IF NOT EXISTS USERS
(
    id              uuid PRIMARY KEY,
    tax_code        varchar(20),
    user_name       varchar(50),
    first_name      varchar(50),
    last_name       varchar(50),
    annual_earnings decimal(15, 2),
    email           varchar(50),
    password        varchar(36),
    address         varchar(50),
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS PRODUCTS
(
    id             uuid PRIMARY KEY,
    product_type   varchar(70),
    product_status varchar(20),
    interest_rate  decimal(6, 4),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ACCOUNTS
(
    id             uuid PRIMARY KEY,
    client_id      uuid,
    IDBA           varchar(20),
    account_status varchar(20),
    balance        decimal(15, 2),
    currency_code  varchar(3),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES USERS (id)
);

CREATE TABLE IF NOT EXISTS TRANSACTIONS
(
    id                      uuid PRIMARY KEY,
    debit_account_id        uuid,
    credit_account_id       uuid,
    transaction_type        varchar(40),
    amount_from             decimal(12, 2),
    amount_to               decimal(12, 2),
    transaction_description varchar(255),
    created_at              TIMESTAMP,
    FOREIGN KEY (debit_account_id) REFERENCES ACCOUNTS (id),
    FOREIGN KEY (credit_account_id) REFERENCES ACCOUNTS (id)
);

CREATE TABLE IF NOT EXISTS AGREEMENTS
(
    id               uuid PRIMARY KEY,
    account_id       uuid,
    product_id       uuid,
    interest_rate    decimal(6, 4),
    currency_code    varchar(3),
    agreement_status varchar(30),
    discount_rate    decimal(4, 2),
    agreement_limit  decimal(15, 2),

    sum              decimal(15, 2),
    original_sum     decimal(15, 2),
    paid_sum        decimal(15, 2),

    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES ACCOUNTS (id),
    FOREIGN KEY (product_id) REFERENCES PRODUCTS (id)
);

CREATE TABLE IF NOT EXISTS CURRENCIES
(
    code_id varchar(3) PRIMARY KEY,
    price   decimal(8, 4)
);