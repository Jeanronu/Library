/**
 * @author Eva Ackley
 * The Vector class represents a mathematical vector in n-dimensional space.
 * A vector can be initialized with a specified size, or with an array of double values.
 * The class provides methods to access and modify individual values, calculate dot product,
 * and convert the vector to a string representation.
 */
public class Vector {
    private double[] vector;
    private int size;

    /**
     * Initializes a new vector with the specified size.
     * @param size the size of the vector
     */
    public Vector(int size) {
        vector = new double[size];
        this.size = size;
    }

    /**
     * Initializes a new vector with the specified array of double values.
     * @param vec the array of double values
     */
    public Vector(double[] vec) {
        vector = vec;
        size = vec.length;
    }

    /**
     * Returns the size of the vector.
     * @return the size of the vector
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Returns the value at the specified index.
     * @param index the index of the value to retrieve
     * @return the value at the specified index
     */
    public double getValue(int index) {
        return vector[index];
    }

    /**
     * Sets the value at the specified index.
     * @param index the index of the value to set
     * @param value the value to set at the specified index
     */
    public void setValue(int index, double value) {
        vector[index] = value;
    }

    /**
     * Calculates the dot product of this vector with the specified vector.
     * The vectors must have the same size, otherwise an IllegalArgumentException is thrown.
     * @param other the vector to calculate the dot product with
     * @return the dot product of the two vectors
     * @throws IllegalArgumentException if the vectors have different sizes
     */
    public double dotProduct(Vector other) throws IllegalArgumentException{
        if (this.size != other.getSize()) {
            throw new IllegalArgumentException("Sizes must match!");
        }
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += vector[i] * other.getValue(i);
        }
        return sum;
    }

    /**
     * Returns a string representation of the vector.
     * The string representation consists of each value of the vector on a new line.
     * @return a string representation of the vector
     */
    public String toString() {
        StringBuilder representation = new StringBuilder();
        for (int i = 0; i < size; i++) {
            representation.append(vector[i]).append("\n");
        }
        return representation.toString();
    }
}