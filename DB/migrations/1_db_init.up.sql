\copy medical_details (id, sap_email, gender, current_address, medical_history, family_medical_history, allergies)
    FROM 'MedicalDetails.csv'
    WITH (FORMAT csv, HEADER);