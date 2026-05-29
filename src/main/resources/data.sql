INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', '$2a$12$oQdd/vEo2dbU0febM/ui4.2yhoSO4AvIll4PQJXKCkjP1zfb6hqS6', 'ADMIN', true);

INSERT INTO Categoria(id, nombre) VALUES(1, 'Bienestar');
INSERT INTO Categoria(id, nombre) VALUES(2, 'Cultura');
INSERT INTO Categoria(id, nombre) VALUES(3, 'Deporte');

INSERT INTO Habito(id, titulo, categoria_id) VALUES(null, 'Meditar', 1);
INSERT INTO Habito(id, titulo, categoria_id) VALUES(null, 'Leer un libro', 2);
INSERT INTO Habito(id, titulo, categoria_id) VALUES(null, 'Hacer ejercicio', 3);

INSERT INTO Recompensa(id, nombre, descripcion, urlImg, nivelRequerido, rareza, activo)
VALUES
(1, 'Iniciado', 'Completa tus primeros hábitos.', '/images/recompensas/lvl5.png', 5, 'COMUN', true),
(2, 'Disciplina de Hierro', 'Mantén una racha de 7 días.', '/images/recompensas/lvl10.png', 10, 'COMUN', true),
(3, 'Guerrero del Hábito', 'Completa 50 hábitos.', '/images/recompensas/lvl15.png', 15, 'RARO', true),
(4, 'Maestro de la Constancia', 'Mantén una racha de 30 días.', '/images/recompensas/lvl20.png', 20, 'RARO', true),
(5, 'Cristal Ascendente', 'Alcanza el nivel 20.', '/images/recompensas/lvl25.png', 25, 'RARO', true),
(6, 'Prestigio Épico', 'Desbloquea más de 100 hábitos.', '/images/recompensas/lvl30.png', 30, 'RARO', true),
(7, 'Titán de la Voluntad', 'Completa hábitos durante 60 días seguidos.', '/images/recompensas/lvl35.png', 35, 'EPICO', true),
(8, 'Diamante Supremo', 'Alcanza el nivel 40.', '/images/recompensas/lvl45.png', 40, 'EPICO', true),
(9, 'Leyenda Habti', 'Completa 500 hábitos.', '/images/recompensas/lvl50.png', 50, 'EPICO', true),
(10, 'Dios de los Hábitos', 'Conquista el máximo prestigio.', '/images/recompensas/lvl55', 60, 'LEGENDARIO', true);