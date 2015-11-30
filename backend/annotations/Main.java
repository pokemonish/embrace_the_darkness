package annotations;

/**
 * Created by fatman on 05/11/15.
 */
public class Main {

    public static void main(String[] args) {
        final MethodContainer methodContainer = new MethodContainer();

        final String notAnnotatedNullableString = methodContainer.getNotAnnotatedNullableString();
        System.out.println("notAnnotatedNullableString's length is " + notAnnotatedNullableString.length());

        final String fakeNotNullString = methodContainer.getFakeNotNullString();
        System.out.println("fakeNotNullString's length is " + fakeNotNullString.length());

        final String nullableString = methodContainer.getNullableString();
        System.out.println("nullableString's length is " + nullableString.length());

        final String notNullString = methodContainer.getNotNullString();
        System.out.println("notNullString's length is " + notNullString.length());

        final MethodContainerChild child = new MethodContainerChild();

        final String childNotNullString = child.getNotNullString();
        System.out.println("childNotNullString's length is " + childNotNullString.length());
    }
}