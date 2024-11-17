CREATE TABLE contas (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        data_vencimento DATE NOT NULL,
                        data_pagamento DATE,
                        valor DECIMAL(15, 2) NOT NULL,
                        descricao VARCHAR(255) NOT NULL,
                        situacao VARCHAR(20) NOT NULL
);

CREATE INDEX idx_data_vencimento ON contas(data_vencimento);
CREATE INDEX idx_situacao ON contas(situacao);