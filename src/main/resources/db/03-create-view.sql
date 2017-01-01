
-- v_price

CREATE OR REPLACE VIEW v_price
AS
SELECT p.stock_id,
       p.f_date AS price_date,
       p.f_value AS price_value
FROM   t_price p;

-- v_stock_price

CREATE OR REPLACE VIEW v_stock_price
AS
SELECT   p.stock_id,
         count(1) AS stock_date_count,
         min(p.price_date) AS stock_first_date,
         max(p.price_date) AS stock_last_date
FROM     v_price p
GROUP BY p.stock_id;

-- v_stock

CREATE OR REPLACE VIEW v_stock
AS
SELECT  s.f_id AS stock_id,
        s.f_name AS stock_name,
        s.f_description AS stock_description,
        p.stock_date_count,
        p.stock_first_date,
        p.stock_last_date
FROM    t_stock s
        LEFT JOIN v_stock_price p ON p.stock_id = s.f_id;

-- v_portfolio

CREATE OR REPLACE VIEW v_portfolio
AS
SELECT p.f_id AS portfolio_id,
       p.f_name AS portfolio_name,
       p.f_start_date AS portfolio_start_date,
       i.stock_id AS indice_id,
       i.stock_name AS indice_name,
       i.stock_description AS indice_description,
       i.stock_date_count AS indice_date_count,
       i.stock_first_date AS indice_first_date,
       i.stock_last_date AS indice_last_date
FROM   t_portfolio p
       JOIN v_stock i ON i.stock_id = p.indice_id;

-- v_holding

CREATE OR REPLACE VIEW v_holding
AS
SELECT h.portfolio_id,
       h.f_quantity AS holding_quantity,
       s.*
FROM   t_holding h
       JOIN v_stock s ON s.stock_id = h.stock_id;
