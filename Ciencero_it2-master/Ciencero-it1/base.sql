


create table usuario(
    usuario_id int primary key auto_increment,
    nombre varchar(128) not null,
    correo varchar(128) not null,
    psswd varchar(128) not null,
    foto varchar(128),
    unique(correo)
    );
 
create table puesto(
    puesto_id int primary key auto_increment,
    nombre varchar(128) not null,
    posX double not null,
    posY double not null,
    unique (nombre));
 
create table categorias(
    puesto_id int not null,
    tipo varchar(16),
    primary key (puesto_id, tipo),
    foreign key (puesto_id) references puesto(puesto_id) ON DELETE CASCADE);
 
create table comentario(
    comentario_id int primary key auto_increment,
    usuario_id int not null,
    puesto_id int not null,
    texto varchar(128) not null,
    foreign key (usuario_id) references usuario(usuario_id) ON DELETE CASCADE,
    foreign key (puesto_id) references puesto(puesto_id) ON DELETE CASCADE);
 
create table calificacion(
    calificacion_id int primary key auto_increment,
    usuario_id int not null,
    puesto_id int not null,
    estrellas int not null,
    foreign key (usuario_id) references usuario(usuario_id) ON DELETE CASCADE,
    foreign key (puesto_id) references puesto(puesto_id) ON DELETE CASCADE);
