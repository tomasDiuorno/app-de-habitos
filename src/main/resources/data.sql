INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', '$2a$12$oQdd/vEo2dbU0febM/ui4.2yhoSO4AvIll4PQJXKCkjP1zfb6hqS6', 'ADMIN', true);

INSERT INTO Categoria(id, nombre) VALUES(1, 'Bienestar');
INSERT INTO Categoria(id, nombre) VALUES(2, 'Cultura');
INSERT INTO Categoria(id, nombre) VALUES(3, 'Deporte');

INSERT INTO Habito(id, titulo, categoria_id) VALUES(null, 'Meditar', 1);
INSERT INTO Habito(id, titulo, categoria_id) VALUES(null, 'Leer un libro', 2);
INSERT INTO Habito(id, titulo, categoria_id) VALUES(null, 'Hacer ejercicio', 3);

INSERT INTO Logro(id, nombre, descripcion) VALUES(null, 'Primer hábito creado', 'Creaste tu primer hábito');

INSERT INTO Logro(id, nombre, descripcion) VALUES(null, '3 hábitos creados', 'Creaste 3 hábitos');

INSERT INTO Logro(id, nombre, descripcion) VALUES(null, '4 hábitos creados', 'Creaste 4 hábitos');