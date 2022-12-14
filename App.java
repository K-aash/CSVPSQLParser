package com.kaash.CSVPSQLParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import javax.transaction.Transaction;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
		BufferedReader reader;
		Configuration con = new Configuration().configure().addAnnotatedClass(Person.class);
        ServiceRegistry reg = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
        SessionFactory sf = con.buildSessionFactory(reg);
        Session session = sf.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        int batchSize = 50;
        int j = 0;
		

		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\Kaash\\Desktop\\C Tools\\Material\\insee_clean.csv"));
			String line = reader.readLine();
			

			while (line != null) {
				for(int i =0;i<=50;i++) {
					if(i>0 && i%batchSize == 0) {
						session.flush();
						session.clear();
					}
						String values[] = line.split(";");
						Person person = new Person();
						person.setId(j);
						person.setSurname(values[0]);
						person.setName(values[1]);
						person.setDob(values[3]);
						person.setDod(values[7]);
				        session.persist(person);
						line = reader.readLine();
						j++;
						if(line==null) {
							session.flush();
							session.clear();
							tx.commit();
							line=reader.readLine();
						}
						
				}
			}
			tx.commit();
			reader.close();
			System.out.println("Parsing successful");
		} catch (IOException e) {
			e.printStackTrace();
		}
}
}
