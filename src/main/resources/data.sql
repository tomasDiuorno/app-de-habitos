INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', '$2a$12$oQdd/vEo2dbU0febM/ui4.2yhoSO4AvIll4PQJXKCkjP1zfb6hqS6', 'ADMIN', true);

-- usuario: tomi99
-- contraseña: Password1!
INSERT INTO Usuario( name, username, email, password, rol, gender, nivelUsuario, activo) VALUES ( 'Tomás', 'tomi99', 'tomi@example.com', '$2a$10$cVhZEtsj3XPcmLfOXbp9eu4XCgfeMqokUVwFAQClH93Bt7bKTQbde', 'USER', 'MASCULINO', 30, true);

INSERT INTO Categoria(id, nombre) VALUES(1, 'Bienestar');
INSERT INTO Categoria(id, nombre) VALUES(2, 'Cultura');
INSERT INTO Categoria(id, nombre) VALUES(3, 'Deporte');

INSERT INTO Habito(id, titulo, categoria_id) VALUES(null, 'Meditar', 1);
INSERT INTO Habito(id, titulo, categoria_id) VALUES(null, 'Leer un libro', 2);
INSERT INTO Habito(id, titulo, categoria_id) VALUES(null, 'Hacer ejercicio', 3);

INSERT INTO Recompensa (nombre, descripcion, urlImg, nivelRequerido, rareza, activo) VALUES
('Café Gratis', 'Canjeá un café o bebida pequeña en cafeterías asociadas.', '/images/recompensas/lvl3.jpg', 3, 'COMUN', true),
('10% OFF Fast Food', 'Descuento en locales de comida rápida adheridos.', '/images/recompensas/lvl5.png', 5, 'COMUN', true),
('Healthy Meal Discount', 'Descuento en comidas saludables y meal prep.', '/images/recompensas/lvl7.png', 7, 'COMUN', true),
('15% OFF Barbería', 'Beneficio en barberías y peluquerías asociadas.', '/images/recompensas/lvl10.png', 10, 'COMUN', true),
('Gym Pass Diario', 'Acceso gratuito por un día a gimnasio asociado.', '/images/recompensas/lvl12.png', 12, 'COMUN', true),
('Bebida Energética', 'Canjeá una bebida energética gratis.', '/images/recompensas/lvl15.png', 15, 'COMUN', true),
('10% OFF Gymwear', 'Descuento en indumentaria deportiva.', '/images/recompensas/lvl18.png', 18, 'COMUN', true),
('Gift Card Gamer Básica', 'Gift card gamer de bajo valor para Steam, PSN o Xbox.', '/images/recompensas/lvl20.png', 20, 'COMUN', true),
('Entrada 2x1 Cine', 'Beneficio 2x1 en entradas seleccionadas.', '/images/recompensas/lvl22.png', 22, 'COMUN', true),
('Cupón Combustible', 'Descuento en estaciones de servicio adheridas.', '/images/recompensas/lvl25.png', 25, 'COMUN', true),
('25% OFF Periféricos Gamer', 'Descuento especial en periféricos gaming.', '/images/recompensas/lvl35.png', 35, 'RARA', true),
('Membresía Gym Semanal', 'Acceso semanal gratuito a gimnasio asociado.', '/images/recompensas/lvl40.png', 40, 'RARA', true),
('Cena Premium', 'Voucher para restaurante premium asociado.', '/images/recompensas/lvl45.png', 45, 'RARA', true),
('Descuento en Zapatillas', 'Beneficio exclusivo en marcas deportivas.', '/images/recompensas/lvl50.png', 50, 'RARA', true),
('Gift Card Gamer Mediana', 'Gift card gamer de valor medio.', '/images/recompensas/lvl55.png', 55, 'RARA', true),
('Entrada Evento Habti', 'Acceso a evento oficial de la comunidad Habti.', '/images/recompensas/lvl60.png', 60, 'RARA', true),
('Curso Online Gratis', 'Acceso a curso de productividad, fitness o tecnología.', '/images/recompensas/lvl65.png', 65, 'RARA', true),
('Meal Prep Fitness', 'Pack de comidas fitness con descuento especial.', '/images/recompensas/lvl70.png', 70, 'RARA', true),
('Entrada VIP Cine/Eventos', 'Acceso VIP para funciones o eventos seleccionados.', '/images/recompensas/lvl75.png', 75, 'RARA', true),
('Cafetería Premium Pass', 'Acceso a bebidas premium por tiempo limitado.', '/images/recompensas/lvl80.png', 80, 'RARA', true),
('Gift Card Gamer Grande', 'Gift card gamer premium de alto valor.', '/images/recompensas/lvl95.png', 95, 'EPICA', true),
('Outfit Deportivo Completo', 'Conjunto deportivo patrocinado.', '/images/recompensas/lvl105.png', 105, 'EPICA', true),
('Membresía Gym Mensual', '1 mes completo en gimnasio asociado.', '/images/recompensas/lvl115.png', 115, 'EPICA', true),
('Cena para Dos', 'Experiencia gastronómica premium para dos personas.', '/images/recompensas/lvl125.png', 125, 'EPICA', true),
('Acceso Exclusivo Habti', 'Acceso privado a eventos y beneficios exclusivos.', '/images/recompensas/lvl135.png', 135, 'EPICA', true),
('Auriculares Gamer', 'Producto físico patrocinado.', '/images/recompensas/lvl145.png', 145, 'EPICA', true),
('Experiencia Wellness', 'Spa, recovery o experiencia de relajación premium.', '/images/recompensas/lvl155.png', 155, 'EPICA', true),
('Suscripción Premium', 'Suscripción a plataforma educativa o de productividad.', '/images/recompensas/lvl165.png', 165, 'EPICA', true),
('Entrada Evento Deportivo/Musical', 'Entrada premium patrocinada.', '/images/recompensas/lvl175.png', 175, 'EPICA', true),
('Meal Plan Premium', 'Plan semanal de alimentación fitness.', '/images/recompensas/lvl185.png', 185, 'EPICA', true),
('Viaje Experiencia Habti', 'Experiencia exclusiva organizada por Habti.', '/images/recompensas/lvl220.png', 220, 'LEGENDARIA', true),
('Setup Gamer Upgrade', 'Upgrade de setup gaming patrocinado.', '/images/recompensas/lvl240.png', 240, 'LEGENDARIA', true),
('Membresía Elite Habti', 'Beneficios exclusivos permanentes.', '/images/recompensas/lvl260.png', 260, 'LEGENDARIA', true),
('Sponsor Mega Pack', 'Caja física premium con productos de sponsors.', '/images/recompensas/lvl280.png', 280, 'LEGENDARIA', true),
('Backstage Evento Exclusivo', 'Acceso backstage y experiencia VIP.', '/images/recompensas/lvl300.png', 300, 'LEGENDARIA', true),
('Gift Card Premium', 'Gift card premium de alto valor.', '/images/recompensas/lvl320.png', 320, 'LEGENDARIA', true),
('Zapatillas Deportivas Premium', 'Zapatillas edición premium patrocinadas.', '/images/recompensas/lvl340.png', 340, 'LEGENDARIA', true),
('Smartphone/Tablet', 'Dispositivo tecnológico patrocinado.', '/images/recompensas/lvl360.png', 360, 'LEGENDARIA', true),
('Membresía Gym Anual', 'Acceso anual a gimnasio asociado.', '/images/recompensas/lvl380.png', 380, 'LEGENDARIA', true),
('Experiencia Premium', 'Karting, track day o actividad exclusiva.', '/images/recompensas/lvl400.png', 400, 'LEGENDARIA', true);