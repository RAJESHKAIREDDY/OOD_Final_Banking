package transactions;

@FunctionalInterface
public interface Payment {
    public void payment(double amount) throws Exception;
}

interface A {
	void a();
}

class B {

	A a = () -> {
		System.out.println("hello");
	};
}
