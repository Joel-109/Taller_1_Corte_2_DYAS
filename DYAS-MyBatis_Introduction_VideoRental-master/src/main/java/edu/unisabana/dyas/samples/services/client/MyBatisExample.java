/*
 * Copyright (C) 2015 cesarvefe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.unisabana.dyas.samples.services.client;



import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.TipoItemMapper;
import edu.unisabana.dyas.samples.entities.Item;
import edu.unisabana.dyas.samples.entities.TipoItem;

/**
 *
 * @author cesarvefe
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException, ParseException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        SqlSession sqlss = sessionfact.openSession();
        ClienteMapper cm=sqlss.getMapper(ClienteMapper.class);
        
        // Consultar Todos los Clientes
        System.out.println("1. Consultar Todos los Clientes");
        System.out.println(cm.consultarClientes());

        // Consultar Clientes por ID
        System.out.println("2. Consultar Cliente por ID");
        System.out.println(cm.consultarClienteDocumento(987654321).toString());

        // Añadir Item Rentado a Cliente
        System.out.println("3. Añadir item rentado a cliente");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date inicio = sdf.parse("2025-08-04");
        Date fin = sdf.parse("2025-08-25");

        cm.agregarItemRentadoACliente(987654321, 2,inicio , fin);

        System.out.println(cm.consultarClientes());
        
        // Insertar Item
        System.out.println("4. Insertar Item");
        ItemMapper im = sqlss.getMapper(ItemMapper.class);

        Date fechaLanzamiento = sdf.parse("2025-04-02");

        Item item = new Item(
            new TipoItem(1, "Electrónico"), 
            4,                             
            "ProtoBoard",                   
            "Tableta para circuitos de 15x20cm", 
            fechaLanzamiento,                        
            50000L,                       
            "Diario",                     
            "Electrodoméstico"              
        );

        im.insertarItem(item);

        // Consultar todos los Items
        System.out.println("5. Consultar todos los Items");
        System.out.println(im.consultarItems());

        // Consultar Item por ID
        System.out.println("6. Consultar item por ID");
        System.out.println(im.consultarItem(3));

        // Obtener todos los tipoItem
        TipoItemMapper tim = sqlss.getMapper(TipoItemMapper.class);

        System.out.println("7. Obtener todos los tipoItem");
        System.out.println(tim.getTiposItems());

        // Obtener tipoItem por ID
        System.out.println("8. Obtener tipoItem por ID");
        System.out.println(tim.getTipoItem(2));

        // Insertar tipoItem
        System.out.println("9. Insertar tipoItem");
        tim.addTipoItem("Accesorios Hogar");

        System.out.println(tim.getTiposItems()); // Acá lo imprimí para mostrar que si se añadió el elemento 
  
        sqlss.commit();
        sqlss.close();        
    }

}
