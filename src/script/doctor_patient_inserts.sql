INSERT INTO users (first_name, last_name, email, password, is_doctor, specialization, medicalLicenseNumber) VALUES
('Lohnes', 'Ark', 'lo.ark@gmail.com', '$2a$12$FaJW791ZnBdUH2WQddw8wOR/BFwGCh7ztLDb5.EeLAMxTQAVz42Zm', TRUE, 'Cardiology', 'MD777777'),
('John', 'Karnel', 'john.karnel@gmail.com', '$2a$12$dEeVx6Q2p5VoV6YJpBXhdOz9ythrBqAk/Lowmu/PUOyF8nT1znz7m', TRUE, 'Neurology', 'MD999999'),
('Kate', 'Vindert', 'kate.vin@gmail.com', '$2a$12$/3VAeLMBC/HrYesld0qwseNPEilKq4hSqenTw5spHeXq4A6XQ.IUS', TRUE, 'Pediatrics', 'MD187353'),
('Raychel', 'Nero', 'r.nero@gmail.com', '$2a$12$J.NlYfpqRJBsWRQzWMuXmeKPRM8nBjud9brrBhU9MGe/LreCtu/qO ', TRUE, 'Dermatology', 'MD983275');

INSERT INTO doctors (user_id, specialization, medicalLicenseNumber)
SELECT id, specialization, medicalLicenseNumber
FROM users
WHERE is_doctor = TRUE;

INSERT INTO users (first_name, last_name, email, password, is_doctor) VALUES
('Wan', 'Lim', 'wan.lim@gmail.com', '$2a$12$GIq4OIqu5p1.oh2AKjXDseylzX5jJQF1B6F4CzEbUQRZ2cgsJCyjO', FALSE),
('Lora', 'Green', 'lora.green@gmail.com', '$2a$12$HWLjrSCzB5Kydw0FFlCvJeb8ZTfH8RuS9VUw2xkhkVDH2S8n8t9vS', FALSE),
('Vector', 'Alien', 'v.alien@gmail.com', '$2a$12$hwb4VKGrRAI4VFLWvkClf.DA0dGDnx52P8LxJYxsopzAFzHVADS5W', FALSE),
('Alina', 'Gronsmen', 'alina.gronsmen@gmail.com', '$2a$12$aqN0XviGNWNFnTAGPVoSU.mJWc0XvxCUX/2HgDnGou/pPGv46iSIG', FALSE),
('Elena', 'Marks', 'elena.marks@gmail.com', '$2a$12$fhBw3WW.ovY8KgG9e49ROeKnRPkn3QLqFpDauFyquBBYQSxmT8Tf6', FALSE),
('Inna', 'Turina', 'inna.turina@gmail.com', '$2a$12$rurcYypYTJxkzELaH/ZWBOlSnHhgnYAYqTWqzh7zZUSC9G80o1jg6', FALSE),
('Ada', 'Pen', 'ada.pen@gmail.com', '$2a$12$6kJJuAX5NW7knIZKhUB2tubo9yTamBEidNbX7ZVANQBs0AUohi6Ue', FALSE),
('Miya', 'Naidzaki', 'miya.naidzaki@gmail.com', '$2a$12$i3d0e.TBjoZ6HlHFx4oewOOuHPAAf056Avq1p5b3bvk2C8k/K8awO', FALSE);
