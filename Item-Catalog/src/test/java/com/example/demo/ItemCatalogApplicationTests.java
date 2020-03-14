package com.example.demo;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemCatalogApplicationTests {

	interface Sounds {
		void speak();
	}

	@Test
	void contextLoads() {
		System.out.println("contextLoads()");
	}

	abstract class Animal {
		private String owner;
		private int weight;
		

		@Override
		public String toString() {
			return "Animal [owner=" + owner + ", weight=" + weight + ", toString()=" + super.toString() + "]";
		}

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}

		public Animal(String owner, int weight) {
			this.owner = owner;
			this.weight = weight;
		}

		protected void move() {
			System.out.println("moving somehow");
		}

		public void breathe() {
			System.out.println("all animals breathe");
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
	}

	class Turtle extends Animal implements Sounds {
		public Turtle(String owner, int weight) {
			super(owner, weight);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void move() {
			System.out.println("moving slow");
		}

		@Override
		public void speak() {
			System.out.println("whatever sound a turtle makes!");

		}
	}

	class Rabbit extends Animal {
		public Rabbit(String owner, int weight) {
			super(owner, weight);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void move() {
			System.out.println("moving really fast");
		}
	}

	@Test
	void testPolymorphism() {
		Turtle t = new Turtle("bob", 10);
		t.move();
		t.breathe();
		Rabbit r = new Rabbit("sam", 5);
		r.move();
		Animal a = t;
		a.move();
		Set<Animal> myAnimals = new HashSet<Animal>();
		myAnimals.add(t);
		myAnimals.add(r);
		myAnimals.add(new Rabbit("mary", 5));
		
		// create a list using Java 8 stream
		
		List<Animal> animalList = myAnimals.stream().collect(Collectors.toList());
		// Comparator comparing + reversed
		animalList.sort(Comparator.comparing(Animal::getOwner).reversed());
		animalList.forEach(System.out::println);
		// Custom comparator
		animalList.sort(new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				if (o1.getOwner() == o2.getOwner()) {
					return 0;
				}
				return o1.getOwner().compareTo(o2.getOwner());
			}
		});
		// lambda sort
		animalList.sort((o1, o2) -> {
			if (o1.getOwner() == o2.getOwner()) {
				return 0;
			}
			return o1.getOwner().compareTo(o2.getOwner());
		});
		
		animalList.sort(Comparator.comparing(Animal::getOwner, (a1, a2) -> {
			return a1.compareTo(a2);
		}));
		
		// sort the list by heaviest on top, then by name A-Z
		/// double sort
		animalList.sort(Comparator.comparing(Animal::getWeight).reversed().thenComparing(Animal::getOwner));
		animalList.forEach(System.out::println);
		
		System.out.println("Filter out weight < 6");
		// filter items from list
		animalList.stream().filter(ob -> ob.weight>6).forEach(System.out::println);
		
	}
}
