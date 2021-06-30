CREATE FUNCTION `GET_LAST_DOSAGE_VISIT`(regimen_name varchar(128), patient_id int)
    RETURNS int
BEGIN
    DECLARE indx int;
    DECLARE visit_types_list varchar(512);
    DECLARE regimen_visits_json varchar(4048);
    DECLARE visit_name_json varchar(64);
    DECLARE prev_visit_dose_number int;
    DECLARE visit_dose_number int;
    DECLARE visit_number int;
    DECLARE last_visit_type varchar(64);

    SET regimen_visits_json =
            (SELECT
                 JSON_EXTRACT(
                         gp.property_value,
                         CONCAT(
                                 REVERSE(
                                         SUBSTRING(
                                                 REVERSE(JSON_UNQUOTE(JSON_SEARCH(gp.property_value, 'one', regimen_name))),
                                                 6
                                             )
                                     ),
                                 '.visits'
                             )
                     ) as VISITS
             FROM global_property gp
             WHERE gp.property = 'cfl.vaccines');

    SET visit_types_list = ',';
    SET indx = 0;
    SET prev_visit_dose_number = -1;
    REPEAT
        SET visit_name_json = JSON_UNQUOTE(JSON_EXTRACT(regimen_visits_json, CONCAT("$[", indx, "].nameOfDose")));
        SET visit_dose_number = CONVERT(JSON_UNQUOTE(JSON_EXTRACT(regimen_visits_json, CONCAT("$[", indx, "].doseNumber"))), UNSIGNED INTEGER);

        IF prev_visit_dose_number < visit_dose_number THEN
            SET visit_types_list = CONCAT(visit_types_list, visit_name_json, ',');
            SET prev_visit_dose_number = visit_dose_number;
            SET last_visit_type = visit_name_json;
        END IF;

        SET indx = indx + 1;
    UNTIL indx = JSON_LENGTH(regimen_visits_json)
        END REPEAT;

    SELECT count(v.visit_id) INTO visit_number
    FROM visit v
             INNER JOIN visit_type vt ON
            vt.visit_type_id = v.visit_type_id
             INNER JOIN visit_attribute va ON
                va.visit_id = v.visit_id
            AND va.voided = 0
             INNER JOIN visit_attribute_type vat ON
                vat.visit_attribute_type_id = va.attribute_type_id
            AND vat.uuid = '70ca70ac-53fd-49e4-9abe-663d4785fe62'
            AND vat.retired = 0
    WHERE v.voided = 0
      AND v.patient_id = patient_id
      AND visit_types_list LIKE CONCAT('%,', vt.name, ',%')
      AND va.value_reference = 'OCCURRED';

    RETURN IF(
                visit_dose_number = visit_number,
                (
                    SELECT rv.visit_id
                    FROM visit rv
                             INNER JOIN visit_type rvt ON rv.visit_type_id = rvt.visit_type_id
                    WHERE rvt.name = last_visit_type and rv.patient_id = patient_id
                ),
                0
        );
END
