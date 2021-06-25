UPDATE messages_template SET calendar_service_query =
        'SELECT 
            bar.EXECUTION_DATE,
            1 AS MESSAGE_ID,
            bar.CHANNEL_ID,
            null AS STATUS_ID
        FROM (
            SELECT
                TIMESTAMP(DATE_ADD(foo.ACTUAL_DATE, INTERVAL DATEDIFF(foo.SELECTED_DATE, foo.ACTUAL_DATE) DAY),:bestContactTime) as EXECUTION_DATE,
                :Service_type AS CHANNEL_ID
            FROM (
                SELECT
                    (SELECT IFNULL(e.encounter_datetime, v.date_started) FROM visit v LEFT JOIN encounter e ON e.visit_id = v.visit_id WHERE v.visit_id = config.last_dosage_visit_id) as ACTUAL_DATE,
                    dl.selected_date as SELECTED_DATE
                FROM
                    DATES_LIST dl
                INNER JOIN messages_template mt ON
                    mt.name = \'Virtual Follow Up\'
                INNER JOIN messages_template_field mtf ON
                    mtf.template_id = mt.messages_template_id
                    AND mtf.uuid = \'1f2a9027-e871-4eaf-92c4-8c99f8dbc380\'
                LEFT JOIN messages_patient_template mpt ON
                    mpt.messages_patient_template_id = mt.messages_template_id
                    AND mpt.patient_id = :patientId
                LEFT JOIN messages_template_field_value mtfv ON
                    mtfv.patient_template_id = mpt.messages_patient_template_id
                    AND mtfv.template_field_id = mtf.messages_template_field_id
                INNER JOIN (
                        SELECT
                            GET_LAST_DOSAGE_VISIT(pa.value, :patientId) as last_dosage_visit_id
                        FROM person_attribute pa
                        INNER JOIN person_attribute_type pat ON
                            pat.person_attribute_type_id  = pa.person_attribute_type_id
                            AND pat.name = \'Vaccination program\'
                        WHERE pa.person_id = :patientId
                    ) config
                WHERE
                    dl.selected_date <= :endDateTime
                    AND dl.selected_date >= :startDateTime
                    AND CONCAT(\',\', IFNULL(mtfv.value, mtf.default_value), \',\') LIKE
                            CONCAT(\'%,\', DATEDIFF(dl.selected_date, (
                                SELECT IFNULL(e.encounter_datetime, v.date_started)
                                FROM visit v
                                LEFT JOIN encounter e ON e.visit_id = v.visit_id
                                WHERE v.visit_id = config.last_dosage_visit_id
                            )), \',%\')
            ) foo
        ) bar
        WHERE
            bar.EXECUTION_DATE <= :endDateTime
            AND bar.EXECUTION_DATE >= :startDateTime
            AND bar.EXECUTION_DATE > GET_PREDICTION_START_DATE_FOR_TEMPLATE(\'Virtual Follow Up\',
                :patientId,
                :actorId,
                :executionStartDateTime)
            AND bar.CHANNEL_ID != \'Deactivate service\'
        UNION
            SELECT
                mssg.msg_send_time AS EXECUTION_DATE,
                1 AS MESSAGE_ID,
                mssg.channel_type AS CHANNEL_ID,
                mss.status AS STATUS_ID
            FROM
                messages_scheduled_service mss
            JOIN messages_patient_template mpt ON
                mpt.messages_patient_template_id = mss.patient_template_id
            JOIN messages_template mt ON
                mt.messages_template_id = mpt.template_id
            JOIN messages_scheduled_service_group mssg ON
                mssg.messages_scheduled_service_group_id = mss.group_id
            WHERE
                mt.name = \'Virtual Follow Up\'
                AND mpt.patient_id = :patientId
                AND mpt.actor_id = :actorId
                AND mssg.patient_id = :patientId
                AND mssg.msg_send_time >= :startDateTime
                AND mssg.msg_send_time <= :endDateTime
            ORDER BY
                1 DESC'
WHERE name = 'Virtual Follow Up';
