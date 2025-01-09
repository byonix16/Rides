package modelo.dominio;

import javax.persistence.EntityManager;
import modelo.*;
import java.util.*;

public class PopulateDB {

	public PopulateDB() {
	}

	private void createAndStoreRidesplusDrivers() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Driver d1 = new Driver();
			Driver d2 = new Driver();

			d1.setEmail("jetxea@gmail.org");
			d1.setName("Jaime");

			d2.setEmail("aotxoa@gmail.org");
			d2.setName("Ane");

			Ride r1 = d1.addRide("Donostia", "Bilbao", new Date(), 4, 10);
			Ride r2 = d1.addRide("Bilbao", "Donostia", new Date(), 6, 4);
			Ride r3 = d2.addRide("Vitoria", "Madrid", new Date(), 16, 14);

			em.persist(d1);
			em.persist(d2);

			em.persist(r1);
			em.persist(r2);
			em.persist(r3);

			Driver d3 = new Driver();
			d3.setEmail("test@ikasle.ehu.eus");
			d3.setName("Test");
			em.persist(d3);

			em.getTransaction().commit();
			System.out.println("Database populated");
		} catch (Exception e) {
			System.out.println("Error in populating database");
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}
//
//	private List<EventoLogin> listaEventos() {
//		EntityManager em = JPAUtil.getEntityManager();
//		try {
//			em.getTransaction().begin();
//			List<EventoLogin> result = em.createQuery("from EventoLogin", EventoLogin.class).getResultList();
//			em.getTransaction().commit();
//			return result;
//		} catch (Exception e) {
//			if (em.getTransaction().isActive()) {
//				em.getTransaction().rollback();
//			}
//			throw e;
//		} finally {
//			em.close();
//		}
//	}

	public static void main(String[] args) {
		PopulateDB e = new PopulateDB();
		e.createAndStoreRidesplusDrivers();
//		PopulateDB e = new PopulateDB();
//
//		System.out.println("Creación de eventos:");
//		e.createAndStoreEventoLogin(1L, "Pepe ha hecho login correctamente", new Date());
//		e.createAndStoreEventoLogin(2L, "Nerea ha intentado hacer login", new Date());
//		e.createAndStoreEventoLogin(3L, "Kepa ha hecho login correctamente", new Date());
//
//		System.out.println("Listado de eventos:");
//		List<EventoLogin> eventos = e.listaEventos();
//
//		for (EventoLogin ev : eventos) {
//			System.out
//					.println("Id: " + ev.getId() + " Descripcion: " + ev.getDescripcion() + " Fecha: " + ev.getFecha());
//		}
	}
}
