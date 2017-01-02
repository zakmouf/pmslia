
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
         count(1) AS date_count,
         min(p.price_date) AS date_first,
         max(p.price_date) AS date_last
FROM     v_price p
GROUP BY p.stock_id;

-- v_stock

CREATE OR REPLACE VIEW v_stock
AS
SELECT  s.f_id AS stock_id,
        s.f_ticker AS stock_ticker,
        s.f_name AS stock_name,
        p.date_count as stock_date_count,
        p.date_first as stock_date_first,
        p.date_last as stock_date_last
FROM    t_stock s
        LEFT JOIN v_stock_price p ON p.stock_id = s.f_id;

-- v_portfolio

CREATE OR REPLACE VIEW v_portfolio
AS
SELECT p.f_id AS portfolio_id,
       p.f_name AS portfolio_name,
       p.f_date_start AS portfolio_date_start,
       i.stock_id AS indice_id,
       i.stock_ticker AS indice_ticker,
       i.stock_name AS indice_name,
       i.stock_date_count AS indice_date_count,
       i.stock_date_first AS indice_date_first,
       i.stock_date_last AS indice_date_last
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
