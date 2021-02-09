package benchmarking;

public class NumberChecker {

	public Boolean isPrime(Integer testNumber) {
		for (Integer i = 2; i < testNumber; i++) {
			if (testNumber % i == 0) return false;
		}
		return true;
	}

	// 첫번째와 두번째를 비교한다
	public Boolean isPrime2(Integer testNumber) {
		int maxToCheck = (int) Math.sqrt(testNumber);
		for (Integer i = 2; i < maxToCheck; i++) {
			if (testNumber % i == 0) return false;
		}
		return true;
	}
	
	
}
