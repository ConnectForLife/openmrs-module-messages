UPDATE messages_template SET service_query =
'SELECT EXECUTION_DATE,
       MESSAGE_ID,
       CHANNEL_ID,
       NULL AS STATUS_ID,
       ADHERENCE_LEVEL,
       ADHERENCE_TREND,
       (SELECT property_value
       FROM global_property
       WHERE property = \'messages.cutOffScoreForMediumLowAdherenceLevel\') as ADHERENCE_LEVEL_CUT_OFF_SCORE_MEDIUM_LOW,
       (SELECT property_value
       FROM global_property
       WHERE property = \'messages.cutOffScoreForHighMediumAdherenceLevel\') as ADHERENCE_LEVEL_CUT_OFF_SCORE_HIGH_MEDIUM,
       (SELECT property_value
       FROM global_property
       WHERE property = \'messages.cutOffScoreForAdherenceTrend\') as ADHERENCE_TREND_CUT_OFF_SCORE,
       (SELECT property_value
       FROM global_property
       WHERE property = \'messages.benchmarkPeriod\') as BENCHMARK_PERIOD,
       IFNULL((SELECT sum(text_response = \'YES\')
       FROM (SELECT * FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\'
       AND    patient_id = :patientId AND actor_id = :actorId) mar
       JOIN   messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
       JOIN   messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
       JOIN   messages_patient_template mpt
       ON     mss.patient_template_id = mpt.messages_patient_template_id
       JOIN   messages_template mt
       ON     mpt.template_id = mt.messages_template_id
       WHERE  mt.name = \'Adherence report daily\' and mar.answered_time >= (select date_add(:executionStartDateTime, interval -
              (select property_value from global_property where property = \'messages.benchmarkPeriod\') day)) and mar.answered_time <= :executionStartDateTime
       OR     mt.name = \'Adherence report weekly\' and mar.answered_time >= (select date_add(:executionStartDateTime, interval -
              (select property_value from global_property where property = \'messages.benchmarkPeriod\') day)) and mar.answered_time <= :executionStartDateTime), 0) as NUMBER_OF_DAYS_USER_GAVE_YES_RESPONSE,
       IFNULL((SELECT sum(text_response = \'YES\' or text_response = \'NO\')
       FROM   (SELECT * FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\'
       AND    patient_id = :patientId AND actor_id = :actorId) mar
       JOIN   messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
       JOIN   messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
       JOIN   messages_patient_template mpt
       ON     mss.patient_template_id = mpt.messages_patient_template_id
       JOIN   messages_template mt
       ON     mpt.template_id = mt.messages_template_id
       WHERE  mt.name = \'Adherence report daily\' and mar.answered_time >= (select date_add(:executionStartDateTime, interval -
              (select property_value from global_property where property = \'messages.benchmarkPeriod\') day)) and mar.answered_time <= :executionStartDateTime
       OR     mt.name = \'Adherence report weekly\' and mar.answered_time >= (select date_add(:executionStartDateTime, interval -
              (select property_value from global_property where property = \'messages.benchmarkPeriod\') day)) and mar.answered_time <= :executionStartDateTime), 0) as NUMBER_OF_DAYS_USER_GAVE_RESPONSE,
       IFNULL((SELECT sum(text_response = \'YES\') / sum(text_response = \'YES\' or text_response = \'NO\')
       FROM   (SELECT * FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\'
       AND    patient_id = :patientId AND actor_id = :actorId) mar
       JOIN   messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
       JOIN   messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
       JOIN   messages_patient_template mpt
       ON     mss.patient_template_id = mpt.messages_patient_template_id
       JOIN   messages_template mt
       ON     mpt.template_id = mt.messages_template_id
       WHERE  mt.name = \'Adherence report daily\' and mar.answered_time >= (select date_add(:executionStartDateTime, interval -
              (select 2 * (select property_value from global_property where property = \'messages.benchmarkPeriod\')) day))
       AND    mar.answered_time <= (select date_add(:executionStartDateTime, interval - (select property_value from global_property where property = \'messages.benchmarkPeriod\') day))
       OR     mt.name = \'Adherence report weekly\' and mar.answered_time >= (select date_add(:executionStartDateTime, interval -
              (select 2 * (select property_value from global_property where property = \'messages.benchmarkPeriod\')) day))
       AND    mar.answered_time <= (select date_add(:executionStartDateTime, interval - (select property_value from global_property where property = \'messages.benchmarkPeriod\') day))), 0) as ADHERENCE_LEVEL_TWO_BENCHMARK_PERIODS_AGO,
       IFNULL((SELECT sum(text_response = \'YES\') / sum(text_response = \'YES\' or text_response = \'NO\')
       FROM   (SELECT * FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\'
       AND    patient_id = :patientId AND actor_id = :actorId) mar
       JOIN   messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
       JOIN   messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
       JOIN   messages_patient_template mpt
       ON     mss.patient_template_id = mpt.messages_patient_template_id
       JOIN   messages_template mt
       ON     mpt.template_id = mt.messages_template_id
       WHERE  mt.name = \'Adherence report daily\' and mar.answered_time >= (select date_add(:executionStartDateTime, interval -
              (select property_value from global_property where property = \'messages.benchmarkPeriod\') day)) and mar.answered_time <= :executionStartDateTime
       OR     mt.name = \'Adherence report weekly\' and mar.answered_time >= (select date_add(:executionStartDateTime, interval -
              (select property_value from global_property where property = \'messages.benchmarkPeriod\') day)) and mar.answered_time <= :executionStartDateTime), 0) as ADHERENCE_LEVEL_BENCHMARK_PERIOD_AGO
FROM   (
              SELECT Timestamp(selected_date, times) AS EXECUTION_DATE,
                     1                               AS MESSAGE_ID,
                     :Service_type                   AS CHANNEL_ID
              FROM   (
                            SELECT *
                            FROM   (
                                          SELECT *
                                          FROM DATES_LIST) dates_list
                            WHERE  (
                                          Dayofmonth(selected_date) <IF (:Frequency_of_the_message = \'Weekly\', 32, 8))
                            AND    :Week_day_of_delivering_message LIKE concat(\'%\',dayname(selected_date),\'%\')) dates
              JOIN
                     (
                            SELECT :bestContactTime times) timeslist) temp
JOIN
       (
              SELECT IF(sum(text_response = \'YES\') / sum(text_response = \'YES\'
              OR     text_response = \'NO\') >
                     (
                            SELECT
                                   (
                                          SELECT property_value
                                          FROM   global_property
                                          WHERE  property = \'messages.cutOffScoreForHighMediumAdherenceLevel\') / 100), \'HIGH\',
                     (
                            SELECT IF(sum(text_response = \'YES\') / sum(text_response = \'YES\'
                            OR     text_response = \'NO\') >
                                   (
                                          SELECT
                                                 (
                                                        SELECT property_value
                                                        FROM   global_property
                                                        WHERE  property = \'messages.cutOffScoreForMediumLowAdherenceLevel\') / 100), \'MEDIUM\', \'LOW\')
                            FROM   (SELECT * FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\'
                            AND    patient_id = :patientId AND actor_id = :actorId) mar
                            JOIN   messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
                            JOIN   messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
                            JOIN   messages_patient_template mpt
                            ON     mss.patient_template_id = mpt.messages_patient_template_id
                            JOIN   messages_template mt
                            ON     mpt.template_id = mt.messages_template_id
                            WHERE  mt.NAME = \'Adherence report daily\'
                            AND    mar.answered_time >=
                                   (
                                          SELECT date_add(:executionStartDateTime, interval -
                                                 (
                                                        SELECT property_value
                                                        FROM   global_property
                                                        WHERE  property = \'messages.benchmarkPeriod\') day))
                            AND    mar.answered_time <= :executionStartDateTime
                            OR     mt.NAME = \'Adherence report weekly\'
                            AND    mar.answered_time >=
                                   (
                                          SELECT date_add(:executionStartDateTime, interval -
                                                 (
                                                        SELECT property_value
                                                        FROM   global_property
                                                        WHERE  property = \'messages.benchmarkPeriod\') day))
                            AND    mar.answered_time <= :executionStartDateTime)) AS ADHERENCE_LEVEL
              FROM   (SELECT * FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\'
              AND    patient_id = :patientId AND actor_id = :actorId) mar
              JOIN   messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
              JOIN   messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
              JOIN   messages_patient_template mpt
              ON     mss.patient_template_id = mpt.messages_patient_template_id
              JOIN   messages_template mt
              ON     mpt.template_id = mt.messages_template_id
              WHERE  mt.NAME = \'Adherence report daily\'
              AND    mar.answered_time >=
                     (
                            SELECT date_add(:executionStartDateTime, interval -
                                   (
                                          SELECT property_value
                                          FROM   global_property
                                          WHERE  property = \'messages.benchmarkPeriod\') day))
              AND    mar.answered_time <= :executionStartDateTime
              OR     mt.NAME = \'Adherence report weekly\'
              AND    mar.answered_time >=
                     (
                            SELECT date_add(:executionStartDateTime, interval -
                                   (
                                          SELECT property_value
                                          FROM   global_property
                                          WHERE  property = \'messages.benchmarkPeriod\') day))
              AND    mar.answered_time <= :executionStartDateTime) adhlev
JOIN
       (
              SELECT IF(sum(text_response = \'YES\') / sum(text_response = \'YES\'
              OR     text_response = \'NO\')         -
                     (
                            SELECT sum(text_response = \'YES\') / sum(text_response = \'YES\'
                            OR     text_response = \'NO\')
                            FROM   (SELECT * FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\'
                            AND    patient_id = :patientId AND actor_id = :actorId) mar
                            JOIN   messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
                            JOIN   messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
                            JOIN   messages_patient_template mpt
                            ON     mss.patient_template_id = mpt.messages_patient_template_id
                            JOIN   messages_template mt
                            ON     mpt.template_id = mt.messages_template_id
                            WHERE  mt.NAME = \'Adherence report daily\'
                            AND    mar.answered_time >=
                                   (
                                          SELECT date_add(:executionStartDateTime, interval -
                                                 (
                                                        SELECT 2 *
                                                               (
                                                        SELECT property_value
                                                        FROM   global_property
                                                        WHERE  property = \'messages.benchmarkPeriod\')) day))
                            AND    mar.answered_time <=
                                   (
                                          SELECT date_add(:executionStartDateTime, interval -
                                                 (
                                                        SELECT property_value
                                                        FROM   global_property
                                                        WHERE  property = \'messages.benchmarkPeriod\') day))
                            OR     mt.NAME = \'Adherence report weekly\'
                            AND    mar.answered_time >=
                                   (
                                          SELECT date_add(:executionStartDateTime, interval -
                                                 (
                                                        SELECT 2 *
                                                               (
                                                                      SELECT property_value
                                                                      FROM   global_property
                                                                      WHERE  property = \'messages.benchmarkPeriod\')) day))
                            AND    mar.answered_time <=
                                   (
                                          SELECT date_add(:executionStartDateTime, interval -
                                                 (
                                                        SELECT property_value
                                                        FROM   global_property
                                                        WHERE  property = \'messages.benchmarkPeriod\') day))) >
                     (
                            SELECT
                                   (
                                          SELECT property_value
                                          FROM   global_property
                                          WHERE  property = \'messages.cutOffScoreForAdherenceTrend\') / 100), \'RISING\',
                     (
                            SELECT IF(sum(text_response = \'YES\') / sum(text_response = \'YES\'
                            OR     text_response = \'NO\')         -
                                   (
                                          SELECT sum(text_response = \'YES\') / sum(text_response = \'YES\'
                                          OR     text_response = \'NO\')
                                          FROM   (SELECT * FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\'
                                          AND    patient_id = :patientId AND actor_id = :actorId) mar
                                          JOIN   messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
                                          JOIN   messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
                                          JOIN   messages_patient_template mpt
                                          ON     mss.patient_template_id = mpt.messages_patient_template_id
                                          JOIN   messages_template mt
                                          ON     mpt.template_id = mt.messages_template_id
                                          WHERE  mt.NAME = \'Adherence report daily\'
                                          AND    mar.answered_time >=
                                                 (
                                                        SELECT date_add(:executionStartDateTime, interval -
                                                               (
                                                                      SELECT 2 *
                                                                             (
                                                                                    SELECT property_value
                                                                                    FROM   global_property
                                                                                    WHERE  property = \'messages.benchmarkPeriod\')) day))
                                          AND    mar.answered_time <=
                                                 (
                                                        SELECT date_add(:executionStartDateTime, interval -
                                                               (
                                                                      SELECT property_value
                                                                      FROM   global_property
                                                                      WHERE  property = \'messages.benchmarkPeriod\') day))
                                          OR     mt.NAME = \'Adherence report weekly\'
                                          AND    mar.answered_time >=
                                                 (
                                                        SELECT date_add(:executionStartDateTime, interval -
                                                               (
                                                                      SELECT 2 *
                                                                             (
                                                                                    SELECT property_value
                                                                                    FROM   global_property
                                                                                    WHERE  property = \'messages.benchmarkPeriod\')) day))
                                          AND    mar.answered_time <=
                                                 (
                                                        SELECT date_add(:executionStartDateTime, interval -
                                                               (
                                                                      SELECT property_value
                                                                      FROM   global_property
                                                                      WHERE  property = \'messages.benchmarkPeriod\') day)) ) <-1 *
                                   (
                                          SELECT
                                                 (
                                                        SELECT property_value
                                                        FROM   global_property
                                                        WHERE  property = \'messages.cutOffScoreForAdherenceTrend\') / 100), \'FALLING\', \'STABLE\'))) AS ADHERENCE_TREND
              FROM (SELECT * FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\'
              AND    patient_id = :patientId AND actor_id = :actorId) mar
              JOIN   messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
              JOIN   messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
              JOIN   messages_patient_template mpt
              ON     mss.patient_template_id = mpt.messages_patient_template_id
              JOIN   messages_template mt
              ON     mpt.template_id = mt.messages_template_id
              WHERE  mt.NAME = \'Adherence report daily\'
              AND    mar.answered_time >=
                     (
                            SELECT date_add(:executionStartDateTime, interval -
                                   (
                                          SELECT property_value
                                          FROM   global_property
                                          WHERE  property = \'messages.benchmarkPeriod\') day))
              AND    mar.answered_time <= :executionStartDateTime
              OR     mt.NAME = \'Adherence report weekly\'
              AND    mar.answered_time >=
                     (
                            SELECT date_add(:executionStartDateTime, interval -
                                   (
                                          SELECT property_value
                                          FROM   global_property
                                          WHERE  property = \'messages.benchmarkPeriod\') day))
              AND    mar.answered_time <= :executionStartDateTime) adhtre
WHERE  EXECUTION_DATE >= :startDateTime
AND    EXECUTION_DATE <= :endDateTime
AND    EXECUTION_DATE > get_prediction_start_date_for_adherence_feedback(:patientId, :actorId, :executionStartDateTime)
AND    CHANNEL_ID != \'Deactivate service\'
UNION
SELECT   mssg.msg_send_time AS EXECUTION_DATE,
         1                  AS MESSAGE_ID,
         mss.channel_type   AS CHANNEL_ID,
         mss.status         AS STATUS_ID,
         NULL               AS ADHERENCE_LEVEL,
         NULL               AS ADHERENCE_TREND,
         NULL               AS ADHERENCE_LEVEL_CUT_OFF_SCORE_MEDIUM_LOW,
         NULL               AS ADHERENCE_LEVEL_CUT_OFF_SCORE_HIGH_MEDIUM,
         NULL               AS ADHERENCE_TREND_CUT_OFF_SCORE,
         NULL               AS BENCHMARK_PERIOD,
         NULL               AS NUMBER_OF_DAYS_USER_GAVE_YES_RESPONSE,
         NULL               AS NUMBER_OF_DAYS_USER_GAVE_RESPONSE,
         NULL               AS ADHERENCE_LEVEL_TWO_BENCHMARK_PERIODS_AGO,
         NULL               AS ADHERENCE_LEVEL_BENCHMARK_PERIOD_AGO
FROM     messages_scheduled_service mss
JOIN     messages_patient_template mpt
ON       mpt.messages_patient_template_id = mss.patient_template_id
JOIN     messages_template mt
ON       mt.messages_template_id = mpt.template_id
JOIN     messages_scheduled_service_group mssg
ON       mssg.messages_scheduled_service_group_id = mss.group_id
WHERE    mt.NAME = \'Adherence feedback\'
AND      mpt.patient_id = :patientId
AND      mpt.actor_id =:actorId
AND      mssg.patient_id = :patientId
AND      mssg.msg_send_time >= :startDateTime
AND      mssg.msg_send_time <= :endDateTime
ORDER BY 1 DESC ; '
WHERE name = 'Adherence feedback';
