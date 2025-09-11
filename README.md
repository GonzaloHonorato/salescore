# SalesCore - Sistema de Gestión de Ventas

## Descripción

SalesCore es un sistema completo de gestión de ventas que permite manejar productos, ventas y detalles de ventas para tiendas de mascotas. El sistema está implementado con Spring Boot y utiliza Oracle Autonomous Database como base de datos.

## Características

- **Gestión completa de CRUD** para:
  - Productos
  - Ventas  
  - Detalles de ventas
- **Base de datos Oracle** con conectividad segura mediante wallet
- **API REST** con endpoints completos
- **Persistencia JPA/Hibernate**
- **Validaciones y manejo de errores**
- **Cálculo automático de ganancias por período**
- **Control de stock de productos**

## Configuración de Base de datos Oracle

### Prerrequisitos

1. Oracle Autonomous Database configurado
2. Wallet de conexión descargado y ubicado en `src/main/resources/wallet/`

### Configuración

1. **Wallet**: El wallet de Oracle ya está incluido en `src/main/resources/wallet/`
2. **Configurar conexión**: Verificar en `application.properties`:
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@ki0i20pz2wfw4ksq_high?TNS_ADMIN=/path/to/wallet
   spring.datasource.username=ADMIN
   spring.datasource.password=IFO:`46gW0.%
   ```

3. **Crear tablas**: Ejecutar el script SQL de configuración:
   ```sql
   -- Ejecutar en Oracle SQL Developer o similar
   @ventas-db-setup.sql
   ```

### Scripts SQL incluidos

- `ventas-db-setup.sql`: Crea todas las tablas, secuencias y datos de prueba

## Estructura del Proyecto

```
src/main/java/com/salescore/salescore/core/ventas/
├── dto/           # Objetos de transferencia de datos
├── mapper/        # Mappers para conversión DTO/Entity
├── model/         # Entidades JPA
├── repository/    # Repositorios Spring Data JPA
├── service/       # Lógica de negocio
└── web/          # Controladores REST
```

## Entidades principales

### Producto
- ID (PK)
- Nombre
- Categoría
- Precio
- Stock
- Descripción

### Venta
- ID (PK)
- Fecha
- Total
- Cliente Nombre
- Cliente RUT
- Vendedor

### DetalleVenta
- ID (PK)
- Venta ID (FK)
- Producto ID (FK)
- Cantidad
- Precio unitario
- Subtotal

## API Endpoints

### Productos
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `GET /api/productos/categoria/{categoria}` - Buscar por categoría
- `GET /api/productos/buscar?nombre={nombre}` - Buscar por nombre
- `POST /api/productos` - Crear producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto

### Ventas
- `GET /api/ventas` - Listar todas las ventas
- `GET /api/ventas/{id}` - Obtener venta por ID
- `GET /api/ventas/fecha?fecha={fecha}` - Ventas por fecha
- `GET /api/ventas/periodo?fechaInicio={fecha}&fechaFin={fecha}` - Ventas por período
- `GET /api/ventas/ganancias/dia?fecha={fecha}` - Ganancias diarias
- `GET /api/ventas/ganancias/mes?año={año}&mes={mes}` - Ganancias mensuales
- `GET /api/ventas/ganancias/año?año={año}` - Ganancias anuales
- `POST /api/ventas` - Crear venta
- `PUT /api/ventas/{id}` - Actualizar venta
- `DELETE /api/ventas/{id}` - Eliminar venta

### Detalles de Venta
- `GET /api/detalles-venta` - Listar todos los detalles
- `GET /api/detalles-venta/{id}` - Obtener detalle por ID
- `GET /api/detalles-venta/venta/{ventaId}` - Detalles por venta
- `GET /api/detalles-venta/producto/{productoId}` - Detalles por producto
- `POST /api/detalles-venta` - Crear detalle
- `PUT /api/detalles-venta/{id}` - Actualizar detalle
- `DELETE /api/detalles-venta/{id}` - Eliminar detalle

## Funcionalidades Especiales

### Control de Stock
- Actualización automática de stock al realizar ventas
- Validación de stock disponible antes de confirmar ventas
- Consultas de productos con stock bajo

### Cálculo de Ganancias
- Ganancias por día específico
- Ganancias por mes y año
- Ganancias por período personalizable
- Totales automáticos por venta

### Reportes
- Ventas por vendedor
- Ventas por cliente
- Productos más vendidos
- Análisis de ventas por categoría

## Ejecución

1. **Compilar el proyecto**:
   ```bash
   ./mvnw clean compile
   ```

2. **Ejecutar la aplicación**:
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Acceder a la aplicación**:
   - URL base: `http://localhost:8081`
   - API endpoints: `http://localhost:8081/api/`

## Configuración adicional

### Hibernate
- DDL auto: `update` (crea/actualiza tablas automáticamente)
- Show SQL: `true` (muestra queries en consola)
- Format SQL: `true` (formatea queries para mejor legibilidad)

### Oracle específico
- Dialect: `OracleDialect`
- Default schema: `ADMIN`
- Character encoding: `UTF-8`
- SSL version: `1.2`

## Datos de prueba

El script `ventas-db-setup.sql` incluye datos de prueba:
- 5 productos de diferentes categorías
- 5 ventas de ejemplo
- 10 detalles de venta

### Productos incluidos:
1. Alimento para perros premium - $25.000
2. Pelota para perros - $5.000
3. Correa para paseo - $12.000
4. Cama para gatos - $18.000
5. Shampoo para mascotas - $8.000

### Vendedores de ejemplo:
- Ana García
- Carlos Rodríguez
- Luis Fernández

## Ejemplos de uso

### Crear una venta
```json
POST /api/ventas
{
  "clienteNombre": "Juan Pérez",
  "clienteRut": "12.345.678-9",
  "vendedor": "Ana García",
  "fecha": "2024-09-11T10:00:00",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2,
      "precioUnitario": 25000,
      "subtotal": 50000
    }
  ],
  "total": 50000
}
```

### Consultar ganancias mensuales
```
GET /api/ventas/ganancias/mes?año=2024&mes=9
```

### Buscar productos por categoría
```
GET /api/productos/categoria/Alimentos
```

## Tecnologías utilizadas

- Spring Boot 3.5.4
- Spring Data JPA
- Oracle Database 19c
- Hibernate
- Lombok
- Maven
- Java 21

## Diferencias con VetCore

SalesCore está basado en la misma arquitectura que VetCore pero adaptado para el dominio de ventas:
- **VetCore**: Gestiona pacientes, consultas y atenciones médicas
- **SalesCore**: Gestiona productos, ventas y detalles de venta

Ambos sistemas comparten:
- Misma configuración de base de datos Oracle
- Arquitectura REST similar
- Patrones de diseño idénticos
- Funcionalidades CRUD completas
