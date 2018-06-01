import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SerializationTest {

    private interface IPerson {
        String getName();
        Collection<IPerson> getFriends();
    }

    private static class Person implements IPerson {
        private String name;
        @Override public String getName() { return this.name; }
        public void setName(String name){ this.name = name; }

        private Collection<IPerson> friends;
        @Override public Collection<IPerson> getFriends() { return friends; }
        public void setFriends(Collection<IPerson> friends) { this.friends = friends; }



        public Person(String name, Collection<IPerson> friends) {
            this.name = name;
            this.friends = friends;
        }

        public Person() {}

    }



    private static final JavaType KLASS;
    private static final SimpleAbstractTypeResolver RESOLVER;



    static {
        KLASS = new ObjectMapper().getTypeFactory().constructType(Person.class);

        RESOLVER = new SimpleAbstractTypeResolver();
        RESOLVER.addMapping(IPerson.class, Person.class);
    }



    @Test
    public void deserializeFromJSONTest() throws Exception {
        String str = "{ \"name\": \"A\", \"friends\":[{\"name\": \"B\", \"friends\":[]},{\"name\": \"C\", \"friends\":null}] }";
        Person person = Serialization.deserialize(str, Serialization.Format.JSON, KLASS, RESOLVER);

        assertPerson(person);
    }

    @Test(expected =  DSLCoreException.class)
    public void deserializeFromInvalidJSONTest() throws DSLCoreException {
        String str = "{ \"name\": \"A\", \"friends\":[{\"name\": \"B\", \"friends\":[]},{\"name\": \"C\", \"friends\":null}";
        Serialization.deserialize(str, Serialization.Format.JSON, KLASS, RESOLVER);
    }

    @Test
    public void serializeToJSONTest() throws Exception {
        Person person = new Person("A", new LinkedList<>());
        person.friends.add(new Person("B", new LinkedList<>()));
        person.friends.add(new Person("C", null));

        String str = Serialization.serialize(person, Serialization.Format.JSON);
        person = Serialization.deserialize(str, Serialization.Format.JSON, KLASS, RESOLVER);

        assertPerson(person);
    }


    @Test
    public void deserializeFromYAMLTest() throws Exception {
        String str = "name: \"A\"\nfriends:\n- name: \"B\"\n  friends: []\n- name: \"C\"\n  friends: null";
        Person person = Serialization.deserialize(str, Serialization.Format.YAML, KLASS, RESOLVER);

        assertPerson(person);
    }

    @Test
    public void deserializeFromInvalidYAMLTest() throws Exception {
        String str = "name: \"A\"\nfriends:\n- name: \"B\"\n  friends: []\n- name: \"C\"\n  friends: ";
        Serialization.deserialize(str, Serialization.Format.YAML, KLASS, RESOLVER);
    }

    @Test
    public void serializeToYAMLTest() throws Exception {
        Person person = new Person("A", new LinkedList<>());
        person.friends.add(new Person("B", new LinkedList<>()));
        person.friends.add(new Person("C", null));

        String str = Serialization.serialize(person, Serialization.Format.YAML);
        person = Serialization.deserialize(str, Serialization.Format.YAML, KLASS, RESOLVER);

        assertPerson(person);
    }


    private void assertPerson(Person person) {
        assertNotNull(person);

        assertEquals("A", person.name);
        assertEquals(2, person.friends.size());

        assertEquals("B", person.friends.stream().findFirst().get().getName());
        assertEquals(0, person.friends.stream().findFirst().get().getFriends().size());

        assertEquals("C", person.friends.stream().skip(1).findFirst().get().getName());
        assertEquals(null, person.friends.stream().skip(1).findFirst().get().getFriends());
    }

}
