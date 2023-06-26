create type request_state as enum(
    'CREATED',
    'APPROVED',
    'COMPLETED',
    'FAILED'
);

create table orderClient (
    id serial primary key,
    status text not null default 'CREATED',
    name text,
    numberPhone text not null,
    amount numeric(10,2),
    orderDate date default current_date
);
-- drop table orderClient;
create table UserTelegram
(
    id serial primary key,
    idTelegram int,
    firstName text,
    lastName text,
    userName text
);

select min(orderDate) from orderClient;
update orderClient set status = 'APPROVED', amount = 1500
where id = 1;


SELECT CONCAT((CURRENT_DATE - INTERVAL '30 days')::date, ' ~ ', max(orderDate), ' amount: ', sum(amount))
FROM orderClient
WHERE orderDate >= CURRENT_DATE - INTERVAL '30 days';

CREATE OR REPLACE FUNCTION validate_order_status()
    RETURNS TRIGGER AS
$$
BEGIN
    IF TG_OP = 'INSERT' THEN
        -- Проверка на добавление новой записи
        IF NEW.status != 'CREATED' THEN
            RAISE EXCEPTION 'Invalid status for new record. Status must be "CREATED".';
END IF;
    ELSIF TG_OP = 'UPDATE' THEN
        -- Проверка на обновление таблицы
        IF OLD.status IN ('COMPLETED', 'FAILED') THEN
            RAISE EXCEPTION 'Cannot update record with status "completed" or "FAILED".';
END IF;

        IF NEW.status = 'CREATED' THEN
            -- Проверка для текущего статуса "created"
            IF NEW.request_state NOT IN ('APPROVED', 'FAILED') THEN
                RAISE EXCEPTION 'Invalid request_state for status "CREATED". Must be either "approved" or "FAILED".';
END IF;

            IF NEW.request_state = 'APPROVED' AND NEW.amount IS NULL THEN
                RAISE EXCEPTION 'Amount must be specified for status "APPROVED".';
END IF;
END IF;
END IF;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER validate_order_status_trigger
    BEFORE INSERT OR UPDATE ON orderClient
                         FOR EACH ROW
                         EXECUTE FUNCTION validate_order_status();
