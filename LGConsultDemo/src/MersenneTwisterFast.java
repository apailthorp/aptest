

/* Pseudo random generator excerpted by Aaron Pailthorp from 
 * http://www.cs.gmu.edu/~sean/research/mersenne/MersenneTwisterFast.java
 * via 
 * http://en.wikipedia.org/wiki/Mersenne_twister
 * 
 * Please see complete version and background at above links for further information
 */

/**
 * <h3>MersenneTwister and MersenneTwisterFast</h3>
 * <p>
 * <b>Version 20</b>, based on version MT199937(99/10/29) of the Mersenne
 * Twister algorithm found at <a
 * href="http://www.math.keio.ac.jp/matumoto/emt.html">
 * 
 * <h3>License</h3>
 * 
 * Copyright (c) 2003 by Sean Luke. <br>
 * Portions copyright (c) 1993 by Michael Lecuyer. <br>
 * All rights reserved. <br>
 * 
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <li>Neither the name of the copyright owners, their employers, nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * </ul>
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * @version 20
 */

public strictfp class MersenneTwisterFast {

	// Period parameters
	private static final int N = 624;
	private static final int M = 397;
	private static final int MATRIX_A = 0x9908b0df; // private static final *
													// constant vector a
	private static final int UPPER_MASK = 0x80000000; // most significant w-r
														// bits
	private static final int LOWER_MASK = 0x7fffffff; // least significant r
														// bits

	// Tempering parameters
	private static final int TEMPERING_MASK_B = 0x9d2c5680;
	private static final int TEMPERING_MASK_C = 0xefc60000;

	private int mt[]; // the array for the state vector
	private int mti; // mti==N+1 means mt[N] is not initialized
	private int mag01[];

	/**
	 * Constructor using the default seed.
	 */
	public MersenneTwisterFast() {
		this(System.currentTimeMillis());
	}

	/**
	 * Constructor using a given seed. Though you pass this seed in as a long,
	 * it's best to make sure it's actually an integer.
	 * 
	 */
	public MersenneTwisterFast(long seed) {
		setSeed(seed);
	}

	/**
	 * Initalize the pseudo random number generator. Don't pass in a long that's
	 * bigger than an int (Mersenne Twister only uses the first 32 bits for its
	 * seed).
	 */
	synchronized public void setSeed(long seed) {
		mt = new int[N];

		mag01 = new int[2];
		mag01[0] = 0x0;
		mag01[1] = MATRIX_A;

		mt[0] = (int) (seed & 0xffffffff);
		for (mti = 1; mti < N; mti++) {
			mt[mti] = (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
		}
	}

	/**
	 * Returns an integer drawn uniformly from 0 to n-1. Suffice it to say, n
	 * must be > 0, or an IllegalArgumentException is raised.
	 */
	public int nextInt(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("n must be positive, got: " + n);

		if ((n & -n) == n) // i.e., n is a power of 2
		{
			int y;

			if (mti >= N) // generate N words at one time
			{
				int kk;
				final int[] mt = this.mt; // locals are slightly faster
				final int[] mag01 = this.mag01; // locals are slightly faster

				for (kk = 0; kk < N - M; kk++) {
					y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
					mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
				}
				for (; kk < N - 1; kk++) {
					y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
					mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
				}
				y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
				mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

				mti = 0;
			}

			y = mt[mti++];
			y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
			y ^= (y << 7) & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
			y ^= (y << 15) & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
			y ^= (y >>> 18); // TEMPERING_SHIFT_L(y)

			return (int) ((n * (long) (y >>> 1)) >> 31);
		}

		int bits, val;
		do {
			int y;

			if (mti >= N) // generate N words at one time
			{
				int kk;
				final int[] mt = this.mt; // locals are slightly faster
				final int[] mag01 = this.mag01; // locals are slightly faster

				for (kk = 0; kk < N - M; kk++) {
					y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
					mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
				}
				for (; kk < N - 1; kk++) {
					y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
					mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
				}
				y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
				mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

				mti = 0;
			}

			y = mt[mti++];
			y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
			y ^= (y << 7) & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
			y ^= (y << 15) & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
			y ^= (y >>> 18); // TEMPERING_SHIFT_L(y)

			bits = (y >>> 1);
			val = bits % n;
		} while (bits - val + (n - 1) < 0);
		return val;
	}

}
