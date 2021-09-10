UPDATE messages_template SET service_query =
    'SELECT 
        bar.EXECUTION_DATE as EXECUTION_DATE,
        1 AS MESSAGE_ID,
        null AS STATUS_ID,
        bar.PATIENT_ID AS PATIENT_ID,
        null AS ACTOR_ID,
        bar.VISIT_TYPE AS VISIT_TYPE,
        bar.DAYS_AFTER_VISIT AS DAYS_AFTER_VISIT
    FROM (
        SELECT
            DATE_ADD(
                (SELECT IFNULL(e.encounter_datetime, v.date_started)
                    FROM visit v
                    LEFT JOIN encounter e ON e.voided = 0 AND e.visit_id = v.visit_id
                    WHERE v.visit_id = foo.LAST_DOSAGE_VISIT_ID
                    ORDER BY IFNULL(e.encounter_datetime, v.date_started) DESC
                    LIMIT 1),
                INTERVAL CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(foo.DAYS_AFTER_VISIT_CFG, \',\', n.`number`), \',\', -1) AS UNSIGNED) DAY
            ) as EXECUTION_DATE,
            foo.PATIENT_ID AS PATIENT_ID,
            (SELECT vt.name FROM visit v INNER JOIN visit_type vt ON vt.visit_type_id = v.visit_type_id WHERE v.visit_id = foo.LAST_DOSAGE_VISIT_ID) AS VISIT_TYPE,
            CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(foo.DAYS_AFTER_VISIT_CFG, \',\', n.`number`), \',\', -1) AS UNSIGNED) AS DAYS_AFTER_VISIT
        FROM (
                SELECT
                    p.patient_id AS PATIENT_ID,
                    (SELECT v.visit_id
                     FROM   visit v
                            INNER JOIN visit_attribute va
                                    ON va.visit_id = v.visit_id
                                       AND va.value_reference = \'true\'
                                       AND va.voided = 0
                            INNER JOIN visit_attribute_type vat
                                    ON vat.visit_attribute_type_id = va.attribute_type_id
                                       AND vat.name = \'isLastDosingVisit\'
                                       AND va.voided = 0
                            INNER JOIN visit_attribute va2
                                    ON va2.visit_id = v.visit_id
                                       AND va2.value_reference = \'OCCURRED\'
                                       AND va2.voided = 0
                            INNER JOIN visit_attribute_type vat2
                                    ON vat2.visit_attribute_type_id = va2.attribute_type_id
                                       AND vat2.name = \'Visit Status\'
                                       AND va2.voided = 0
                     WHERE  v.voided = 0
                            AND v.patient_id = p.patient_id
                     ORDER  BY v.date_started DESC
                     LIMIT  1 )  AS LAST_DOSAGE_VISIT_ID,
                    IFNULL(mtfv.value, mtf.default_value) AS DAYS_AFTER_VISIT_CFG
                FROM
                    patient p
                INNER JOIN messages_template mt ON
                    mt.name = \'Virtual Follow Up\'
                INNER JOIN messages_template_field mtf ON
                    mtf.template_id = mt.messages_template_id
                    AND mtf.uuid = \'1f2a9027-e871-4eaf-92c4-8c99f8dbc380\'
                LEFT JOIN messages_patient_template mpt ON
                    mpt.messages_patient_template_id = mt.messages_template_id
                    AND p.patient_id = mpt.patient_id
                LEFT JOIN messages_template_field_value mtfv ON
                    mtfv.patient_template_id = mpt.messages_patient_template_id
                    AND mtfv.template_field_id = mtf.messages_template_field_id
                INNER JOIN person_attribute_type pat ON
                    pat.name = \'Vaccination program\'
                INNER JOIN person_attribute pa ON
                    pa.person_id = p.patient_id
                    AND pa.person_attribute_type_id = pat.person_attribute_type_id
                WHERE
                    p.voided = 0
            ) foo
        INNER JOIN NUMBERS_100 n ON
            CHAR_LENGTH(foo.DAYS_AFTER_VISIT_CFG)-CHAR_LENGTH(REPLACE(foo.DAYS_AFTER_VISIT_CFG, \',\', \'\')) >= n.`number`-1
    ) bar
    WHERE
        bar.EXECUTION_DATE >= :startDateTime
        AND bar.EXECUTION_DATE <= :endDateTime
    UNION
        SELECT
            mssg.msg_send_time AS EXECUTION_DATE,
            1 AS MESSAGE_ID,
            mss.status AS STATUS_ID,
            mpt.patient_id AS PATIENT_ID,
            mpt.actor_id AS ACTOR_ID,
            null AS VISIT_TYPE,
            null AS DAYS_AFTER_VISIT
        FROM
            messages_scheduled_service mss
        JOIN
            messages_patient_template mpt ON mpt.messages_patient_template_id = mss.patient_template_id
        JOIN
            messages_template mt ON mt.messages_template_id = mpt.template_id
        JOIN
            messages_scheduled_service_group mssg ON mssg.messages_scheduled_service_group_id = mss.group_id
        WHERE
            mt.name = \'Virtual Follow Up\'
            AND mpt.patient_id = mpt.patient_id
            AND mpt.actor_id = mpt.actor_id
            AND mssg.patient_id = mpt.patient_id
            AND mssg.msg_send_time >= :startDateTime
            AND mssg.msg_send_time <= :endDateTime
        ORDER BY
            1 DESC'
WHERE name = 'Virtual Follow Up';