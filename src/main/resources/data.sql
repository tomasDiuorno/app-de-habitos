INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', '$2a$12$oQdd/vEo2dbU0febM/ui4.2yhoSO4AvIll4PQJXKCkjP1zfb6hqS6', 'ADMIN', true);

-- usuario: tomi99
-- contraseña: Password1!
INSERT INTO Usuario( name, username, email, password, rol, gender, nivelUsuario, activo) VALUES ( 'Tomás', 'tomi99', 'tomi@example.com', '$2a$10$cVhZEtsj3XPcmLfOXbp9eu4XCgfeMqokUVwFAQClH93Bt7bKTQbde', 'USER', 'MASCULINO', 30, true);

INSERT INTO Usuario(id, name, username, email, password, rol, gender, nivelUsuario, activo) VALUES ('22', 'Lucas', 'Lucas22', 'lucas@example.com', '$2a$10$cVhZEtsj3XPcmLfOXbp9eu4XCgfeMqokUVwFAQClH93Bt7bKTQbde', 'USER', 'MASCULINO', 30, true);

INSERT INTO Monedero(id, saldo, usuario_id)
VALUES (22, 250, 22);

INSERT INTO Categoria(id, nombre) VALUES(1, 'Bienestar');
INSERT INTO Categoria(id, nombre) VALUES(2, 'Cultura');
INSERT INTO Categoria(id, nombre) VALUES(3, 'Deporte');

INSERT INTO Logro(nombre, descripcion, condicion) VALUES ('Primer Paso', 'Agregaste tu primer hábito', 'PRIMER_HABITO');
INSERT INTO Logro(nombre, descripcion, condicion) VALUES ('Constante', 'Llegaste a 3 hábitos activos', 'TRES_HABITOS');
INSERT INTO Logro(nombre, descripcion, condicion) VALUES ('Experto', 'Llegaste a 4 hábitos activos', 'CUATRO_HABITOS');

INSERT INTO Recompensa (nombre, descripcion, urlImg, nivelRequerido, rareza) VALUES
('Café Gratis', 'Canjeá un café o bebida pequeña en cafeterías asociadas.', '/images/recompensas/lvl3.jpg', 3, 'COMUN'),
('10% OFF Fast Food', 'Descuento en locales de comida rápida adheridos.', '/images/recompensas/lvl5.png', 5, 'COMUN'),
('Healthy Meal Discount', 'Descuento en comidas saludables y meal prep.', '/images/recompensas/lvl7.png', 7, 'COMUN'),
('10 Monedas', 'Canjeá 10 monedas para usar en la tienda.', '/images/recompensas/lvl120.png', 10, 'COMUN'),
('Gym Pass Diario', 'Acceso gratuito por un día a gimnasio asociado.', '/images/recompensas/lvl12.png', 12, 'COMUN'),
('Bebida Energética', 'Canjeá una bebida energética gratis.', '/images/recompensas/lvl15.png', 15, 'COMUN'),
('10% OFF Gymwear', 'Descuento en indumentaria deportiva.', '/images/recompensas/lvl18.png', 18, 'COMUN'),
('Gift Card Gamer Básica', 'Gift card gamer de bajo valor para Steam, PSN o Xbox.', '/images/recompensas/lvl20.png', 20, 'COMUN'),
('Entrada 2x1 Cine', 'Beneficio 2x1 en entradas seleccionadas.', '/images/recompensas/lvl22.png', 22, 'COMUN'),
('Cupón Combustible', 'Descuento en estaciones de servicio adheridas.', '/images/recompensas/lvl25.png', 25, 'COMUN'),
('10 Monedas', 'Canjeá 10 monedas para usar en la tienda.', '/images/recompensas/lvl120.png', 30, 'COMUN'),
('25% OFF Periféricos Gamer', 'Descuento especial en periféricos gaming.', '/images/recompensas/lvl35.png', 35, 'RARA'),
('Membresía Gym Semanal', 'Acceso semanal gratuito a gimnasio asociado.', '/images/recompensas/lvl40.png', 40, 'RARA'),
('Cena Premium', 'Voucher para restaurante premium asociado.', '/images/recompensas/lvl45.png', 45, 'RARA'),
('Descuento en Zapatillas', 'Beneficio exclusivo en marcas deportivas.', '/images/recompensas/lvl50.png', 50, 'RARA'),
('Gift Card Gamer Mediana', 'Gift card gamer de valor medio.', '/images/recompensas/lvl55.png', 55, 'RARA'),
('Entrada Evento Habti', 'Acceso a evento oficial de la comunidad Habti.', '/images/recompensas/lvl60.png', 60, 'RARA'),
('Curso Online Gratis', 'Acceso a curso de productividad, fitness o tecnología.', '/images/recompensas/lvl65.png', 65, 'RARA'),
('Meal Prep Fitness', 'Pack de comidas fitness con descuento especial.', '/images/recompensas/lvl70.png', 70, 'RARA'),
('Entrada VIP Cine/Eventos', 'Acceso VIP para funciones o eventos seleccionados.', '/images/recompensas/lvl75.png', 75, 'RARA'),
('Cafetería Premium Pass', 'Acceso a bebidas premium por tiempo limitado.', '/images/recompensas/lvl80.png', 80, 'RARA'),
('Gift Card Gamer Grande', 'Gift card gamer premium de alto valor.', '/images/recompensas/lvl95.png', 95, 'EPICA'),
('Outfit Deportivo Completo', 'Conjunto deportivo patrocinado.', '/images/recompensas/lvl105.png', 105, 'EPICA'),
('Membresía Gym Mensual', '1 mes completo en gimnasio asociado.', '/images/recompensas/lvl115.png', 115, 'EPICA'),
('Cena para Dos', 'Experiencia gastronómica premium para dos personas.', '/images/recompensas/lvl125.png', 125, 'EPICA'),
('Acceso Exclusivo Habti', 'Acceso privado a eventos y beneficios exclusivos.', '/images/recompensas/lvl135.png', 135, 'EPICA'),
('Auriculares Gamer', 'Producto físico patrocinado.', '/images/recompensas/lvl145.png', 145, 'EPICA'),
('Experiencia Wellness', 'Spa, recovery o experiencia de relajación premium.', '/images/recompensas/lvl155.png', 155, 'EPICA'),
('Suscripción Premium', 'Suscripción a plataforma educativa o de productividad.', '/images/recompensas/lvl165.png', 165, 'EPICA'),
('Entrada Evento Deportivo/Musical', 'Entrada premium patrocinada.', '/images/recompensas/lvl175.png', 175, 'EPICA'),
('Meal Plan Premium', 'Plan semanal de alimentación fitness.', '/images/recompensas/lvl185.png', 185, 'EPICA'),
('Viaje Experiencia Habti', 'Experiencia exclusiva organizada por Habti.', '/images/recompensas/lvl220.png', 220, 'LEGENDARIA'),
('Setup Gamer Upgrade', 'Upgrade de setup gaming patrocinado.', '/images/recompensas/lvl240.png', 240, 'LEGENDARIA'),
('Membresía Elite Habti', 'Beneficios exclusivos permanentes.', '/images/recompensas/lvl260.png', 260, 'LEGENDARIA'),
('Sponsor Mega Pack', 'Caja física premium con productos de sponsors.', '/images/recompensas/lvl280.png', 280, 'LEGENDARIA'),
('Backstage Evento Exclusivo', 'Acceso backstage y experiencia VIP.', '/images/recompensas/lvl300.png', 300, 'LEGENDARIA'),
('Gift Card Premium', 'Gift card premium de alto valor.', '/images/recompensas/lvl320.png', 320, 'LEGENDARIA'),
('Zapatillas Deportivas Premium', 'Zapatillas edición premium patrocinadas.', '/images/recompensas/lvl340.png', 340, 'LEGENDARIA'),
('Smartphone/Tablet', 'Dispositivo tecnológico patrocinado.', '/images/recompensas/lvl360.png', 360, 'LEGENDARIA'),
('Membresía Gym Anual', 'Acceso anual a gimnasio asociado.', '/images/recompensas/lvl380.png', 380, 'LEGENDARIA'),
('Experiencia Premium', 'Karting, track day o actividad exclusiva.', '/images/recompensas/lvl400.png', 400, 'LEGENDARIA');

INSERT IGNORE INTO Bonificacion
(id, nombre, descripcion, porcentaje, precioMonedas, duracionEnDias, disponible)
VALUES
(1, 'IMPULSO INICIAL', 'Sumá una bonificación pequeña para avanzar un poco más rápido de nivel.', 25, 50, 1, true),
(2, 'IMPULSO MEDIO', 'Obtené una mejora equilibrada para reforzar tu progreso diario y sostener mejor tu rutina.',50, 90, 1, true),
(3, 'IMPULSO MÁXIMO', 'Aplicá la bonificación más alta para conseguir un avance mayor en tus objetivos.', 100, 130, 1, true);

UPDATE Monedero
SET saldo = 250
WHERE usuario_id = 22;