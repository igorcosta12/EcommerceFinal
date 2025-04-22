CREATE TABLE IF NOT EXISTS users (
                                     uuid TEXT PRIMARY KEY,
                                     name TEXT NOT NULL,
                                     email TEXT NOT NULL UNIQUE,
                                     password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
                                        uuid TEXT PRIMARY KEY,
                                        name TEXT NOT NULL,
                                        price REAL NOT NULL
);


CREATE TABLE IF NOT EXISTS sale (
                                    uuid TEXT PRIMARY KEY,
                                    user_uuid TEXT NOT NULL,
                                    payment_method TEXT NOT NULL,
                                    total REAL NOT NULL,
                                    FOREIGN KEY (user_uuid) REFERENCES users (uuid)
);

CREATE TABLE IF NOT EXISTS sale_items (
                                          sale_id TEXT NOT NULL,
                                          product_uuid TEXT NOT NULL,
                                          PRIMARY KEY (sale_id, product_uuid),
                                          FOREIGN KEY (sale_id) REFERENCES sale (uuid),
                                          FOREIGN KEY (product_uuid) REFERENCES products (uuid)
);

INSERT INTO users (uuid, name, email, password) VALUES
                                                    ('d4e8ea8b-63e9-47fd-b3d7-bdfd755d51d2', 'Igor Silva', 'igor@gmail.com', 'senha123'),
                                                    ('76b8ac57-d99e-43c4-9284-6fc9f1a1a1c3', 'Ana Souza', 'ana112@gmail.com', 'senha456');

INSERT INTO products (uuid, name, price) VALUES
                                             ('123e4567-e89b-12d3-a456-426614174000', 'Produto 1', 100.0),
                                             ('123e4567-e89b-12d3-a456-426614174001', 'Produto 2', 200.0),
                                             ('123e4567-e89b-12d3-a456-426614174002', 'Produto 3', 150.0);

INSERT INTO sale (uuid, user_uuid, payment_method, total) VALUES
                                                              ('5b6d85d5-5bde-42c1-8d1d-062e1b59e5c1', 'd4e8ea8b6-3e99-47fd-b3d7-bdfd755d51d2', 'Cartão de Crédito', 450.0),
                                                              ('9b9f8e2f-ff9b-4b61-9a1f-c7071b063e4b', '76b8ac57-d99e-43c4-9284-6fc9f1a1a1c3', 'Boleto', 300.0);

INSERT INTO sale_items (sale_id, product_uuid) VALUES
                                                   ('5b6d85d5-5bde-42c1-8d1d-062e1b59e5c1', '123e4567-e89b-12d3-a456-426614174001'),
                                                   ('5b6d85d5-5bde-42c1-8d1d-062e1b59e5c1', '123e4567-e89b-12d3-a456-426614174002'),
                                                   ('9b9f8e2f-ff9b-4b61-9a1f-c7071b063e4b', '123e4567-e89b-12d3-a456-426614174000');
