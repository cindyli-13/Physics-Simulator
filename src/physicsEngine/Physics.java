package physicsEngine;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Circle;
import objects.Entity;
import objects.Rectangle;

/**
 * This class contains the static methods which govern 
 * the physics of the simulation.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, April 17th, 2018
 */
public class Physics {
	
	/**
	 * Manages the collision detection and collision response between 
	 * all possible collision scenarios.
	 * 
	 * @param entities   	the ArrayList containing the entities to check for collision
	 * @param ground 	 	the boundary bordering the bottom of the simulation window
	 * @param leftBoundary  the boundary bordering the left of the simulation window
	 * @param topBoundary 	the boundary bordering the top of the simulation window
	 * @param rightBoundary  	the boundary bordering the right of the simulation window
	 * @param z 		 	the z-value of all entities
	 */
	public static void collision(ArrayList<Entity> entities, Rectangle ground, Rectangle leftBoundary, 
			Rectangle topBoundary, Rectangle rightBoundary, float z) {
			
		// entity-entity collision
		for (int i = 0; i < entities.size(); i++) {
			for (int j = i + 1; j < entities.size(); j++) {
					
				Entity a = entities.get(i);
				Entity b = entities.get(j);
					
					
				// if collision is detected
				if (a.intersects(b)) {
					
					// if A is Rectangle
					if (a instanceof Rectangle) {
							
						// if B is Rectangle
						if (b instanceof Rectangle) {
								
							collisionRectangleRectangle((Rectangle) a, (Rectangle) b);
						}
							
						// if B is Circle
						if (b instanceof Circle) {
								
							collisionRectangleCircle((Rectangle) a, (Circle) b);
						}
					}
						
					// if A is Circle
					if (a instanceof Circle) {
							
						// if B is Rectangle
						if (b instanceof Rectangle) {
								
							collisionRectangleCircle((Rectangle) b, (Circle) a);
						}
							
						// if B is Circle
						if (b instanceof Circle) {
								
							collisionCircleCircle((Circle) a, (Circle) b);
						}
					}
				}
					
			}
		}
			
		// entity-boundary collision
		for (Entity entity:entities) {
				
			// if entity is rectangle
			if (entity instanceof Rectangle) {
					
				Rectangle r = (Rectangle) entity;
					
				// ground
				if (r.getAabb().intersects(ground.getAabb())) {
					
					r.setPosition(new Vector3f(r.getPosition().x, 
							ground.getAabb().getMax().y + r.getHeight()/2, z));
					r.setVelocity(new Vector3f(r.getVelocity().x, 
							r.getVelocity().y * r.getCoefficientOfRestitution(), 0));
					
					// friction
					float mu = pythagoreanTheorem(r.getKineticFriction(), ground.getKineticFriction());
					float magnitude = mu * 9.81f;
					
					if (r.getVelocity().x > 0) {
						
						if (r.getVelocity().x < magnitude)
							r.getVelocity().x = 0f;
						else
							r.getVelocity().x -= magnitude;
					}
					else if (r.getVelocity().x < 0) {
						
						if (r.getVelocity().x > -magnitude)
							r.getVelocity().x = 0f;
						else
							r.getVelocity().x += magnitude;
					}
				}
					
				// left boundary
				if (r.getAabb().intersects(leftBoundary.getAabb())) {
					r.setPosition(new Vector3f(leftBoundary.getAabb().getMax().x + r.getWidth()/2, 
							r.getPosition().y, z));
					r.setVelocity(new Vector3f(r.getVelocity().x * r.getCoefficientOfRestitution(), 
							r.getVelocity().y, 0));
				}
					
				// top boundary
				if (r.getAabb().intersects(topBoundary.getAabb())) {
					r.setPosition(new Vector3f(r.getPosition().x, 
							topBoundary.getAabb().getMin().y - r.getHeight()/2, z));
					r.setVelocity(new Vector3f(r.getVelocity().x, 
							r.getVelocity().y * r.getCoefficientOfRestitution(), 0));
				}
					
				// right boundary
				if (r.getAabb().intersects(rightBoundary.getAabb())) {
					r.setPosition(new Vector3f(rightBoundary.getAabb().getMin().x - r.getWidth()/2, 
							r.getPosition().y, z));
					r.setVelocity(new Vector3f(r.getVelocity().x * r.getCoefficientOfRestitution(), 
							r.getVelocity().y, 0));
				}
			}
				
			// if entity is circle
			else if (entity instanceof Circle) {
					
				Circle c = (Circle) entity;
					
				// ground
				if (c.intersects(ground.getAabb())) {
					c.setPosition(new Vector3f(c.getPosition().x, 
							ground.getAabb().getMax().y + c.getRadius(), z));
					c.setVelocity(new Vector3f(c.getVelocity().x, 
							c.getVelocity().y * c.getCoefficientOfRestitution(), 0));
					
					// friction
					float mu = pythagoreanTheorem(c.getKineticFriction(), ground.getKineticFriction());
					float magnitude = mu * 9.81f;
					
					if (c.getVelocity().x > 0) {
						
						if (c.getVelocity().x < magnitude)
							c.getVelocity().x = 0f;
						else
							c.getVelocity().x -= magnitude;
					}
					else if (c.getVelocity().x < 0) {
						
						if (c.getVelocity().x > -magnitude)
							c.getVelocity().x = 0f;
						else
							c.getVelocity().x += magnitude;
					}
				}
					
				// left boundary
				if (c.intersects(leftBoundary.getAabb())) {
					c.setPosition(new Vector3f(leftBoundary.getAabb().getMax().x + c.getRadius(), 
							c.getPosition().y, z));
					c.setVelocity(new Vector3f(c.getVelocity().x * c.getCoefficientOfRestitution(), 
							c.getVelocity().y, 0));
				}
					
				// top boundary
				if (c.intersects(topBoundary.getAabb())) {
					c.setPosition(new Vector3f(c.getPosition().x, 
							topBoundary.getAabb().getMin().y - c.getRadius(), z));
					c.setVelocity(new Vector3f(c.getVelocity().x, 
							c.getVelocity().y * c.getCoefficientOfRestitution(), 0));
				}
					
				// right boundary
				if (c.intersects(rightBoundary.getAabb())) {
					c.setPosition(new Vector3f(rightBoundary.getAabb().getMin().x - c.getRadius(), 
							c.getPosition().y, z));
					c.setVelocity(new Vector3f(c.getVelocity().x * c.getCoefficientOfRestitution(), 
							c.getVelocity().y, 0));
				}
			}
				
		}
			
	}
		
	/**
	 * Manages rectangle-rectangle collision.
	 * 
	 * @param a  the first rectangle
	 * @param b  the second rectangle
	 */
	public static void collisionRectangleRectangle(Rectangle a, Rectangle b) {
			
		// the collision normal is the vector whose direction is the 
		// direction the collision will resolve in
		Vector3f collisionNormal;
		
		// horizontal penetration
		float horizontal = Math.min(a.getAabb().getMax().x - b.getAabb().getMin().x, 
				b.getAabb().getMax().x - a.getAabb().getMin().x);
		
		// vertical penetration
		float vertical = Math.min(a.getAabb().getMax().y - b.getAabb().getMin().y, 
				b.getAabb().getMax().y - a.getAabb().getMin().y);
		
		// if objects are colliding horizontally
		if (horizontal < vertical) {
				
			// if A is to the left of B
			if (a.getPosition().x < b.getPosition().x) {
					
				// positional correction
				a.getPosition().sub(horizontal / 2, 0, 0);
				b.getPosition().add(horizontal / 2, 0, 0);
					
				// set collision normal (direction A should travel)
				collisionNormal = new Vector3f(1,0,0);
			}
				
			// if A is to the right of B
			else {
					
				// positional correction
				a.getPosition().add(horizontal / 2, 0, 0);
				b.getPosition().sub(horizontal / 2, 0, 0);
					
				// set collision normal (direction A should travel)
				collisionNormal = new Vector3f(-1,0,0);
			}
				
		}
			
		// if objects are colliding diagonally
		else if (horizontal == vertical ){
				
			// if A is to the left of B
			if (a.getPosition().x < b.getPosition().x) {

				// positional correction
				a.getPosition().sub(horizontal / 2, vertical / 2, 0);
				b.getPosition().add(horizontal / 2, vertical / 2, 0);
					
				// set collision normal (direction A should travel)
				collisionNormal = new Vector3f(1,1,0);
			}
				
			// if A is to the right of B
			else {
					
				// positional correction
				a.getPosition().add(horizontal / 2, vertical / 2, 0);
				b.getPosition().sub(horizontal / 2, vertical / 2, 0);
					
				// set collision normal (direction A should travel)
				collisionNormal = new Vector3f(-1,-1,0);
			}
				
		}
			
		// if objects are colliding vertically
		else {
				
			// if A is to the bottom of B
			if (a.getPosition().y < b.getPosition().y) {
					
				// positional correction
				a.getPosition().sub(0, vertical / 2, 0);
				b.getPosition().add(0, vertical / 2, 0);
					
				// set collision normal (direction A should travel)
				collisionNormal = new Vector3f(0,1,0);
			}
			
			// if A is to the top of B
			else {
					
				// positional correction
				a.getPosition().add(0, vertical / 2, 0);
				b.getPosition().sub(0, vertical / 2, 0);
					
				// set collision normal (direction A should travel)
				collisionNormal = new Vector3f(0,-1,0);
			}
				
		}
			
		// impulse resolution
		impulseResolution(collisionNormal, a, b);
	}
		
	/**
	 * Manages rectangle-circle collision.
	 * 
	 * @param a  the rectangle
	 * @param b  the circle
	 */
	public static void collisionRectangleCircle(Rectangle a, Circle b) {
			
		// collision normal
			
		Vector3f closest = new Vector3f(b.getPosition().x, b.getPosition().y, b.getPosition().z);
			
		closest.x = Physics.clamp(closest.x, a.getPosition().x - a.getWidth()/2, a.getPosition().x + a.getWidth()/2);
		closest.y = Physics.clamp(closest.y, a.getPosition().y - a.getHeight()/2, a.getPosition().y + a.getHeight()/2);
			
		Vector3f collisionNormal = new Vector3f();
		b.getPosition().sub(closest, collisionNormal);
			
			
		// find penetration vector along collision normal
			
		float distance = collisionNormal.length();
			
		float rX = collisionNormal.x * b.getRadius() / distance;
		float rY = collisionNormal.y * b.getRadius() / distance;
			
		Vector3f radius = new Vector3f(rX, rY, b.getPosition().z - a.getPosition().z);
			
		Vector3f penetration = new Vector3f();
		radius.sub(collisionNormal, penetration);
			
			
		// positional correction
			
		Vector3f newPos = new Vector3f(
			a.getPosition().x - penetration.x / 2, 
			a.getPosition().y - penetration.y / 2,
			a.getPosition().z - penetration.z / 2);
		a.setPosition(newPos);
			
		newPos = new Vector3f(
			b.getPosition().x + penetration.x / 2, 
			b.getPosition().y + penetration.y / 2,
			b.getPosition().z + penetration.z / 2);
		b.setPosition(newPos);
			
			
		// impulse resolution
		collisionNormal.normalize();
		impulseResolution(collisionNormal, a, b);
	}

	/**
	 * Manages circle-circle collision.
	 * 
	 * @param a  the first circle
	 * @param b  the second circle
	 */
	public static void collisionCircleCircle(Circle a, Circle b) {
			
		// collision normal
		Vector3f collisionNormal = new Vector3f();
		b.getPosition().sub(a.getPosition(), collisionNormal);
			
		
		// find penetration vector along collision normal
			
		float distance = collisionNormal.length();
			
		float rX = collisionNormal.x * a.getRadius() / distance;
		float rY = collisionNormal.y * a.getRadius() / distance;
			
		Vector3f radius1 = new Vector3f(rX, rY, b.getPosition().z - a.getPosition().z);
			
		rX = collisionNormal.x * b.getRadius() / distance;
		rY = collisionNormal.y * b.getRadius() / distance;
					
		Vector3f radius2 = new Vector3f(rX, rY, b.getPosition().z - a.getPosition().z);
			
		radius1.add(radius2);
			
		Vector3f penetration = new Vector3f();
		radius1.sub(collisionNormal, penetration);
			
			
		// positional correction
			
		Vector3f newPos = new Vector3f(
			a.getPosition().x - penetration.x / 2, 
			a.getPosition().y - penetration.y / 2,
			a.getPosition().z - penetration.z / 2);
		a.setPosition(newPos);
					
		newPos = new Vector3f(
			b.getPosition().x + penetration.x / 2, 
			b.getPosition().y + penetration.y / 2,
			b.getPosition().z + penetration.z / 2);
		b.setPosition(newPos);
			
			
		// impulse resolution
		collisionNormal.normalize();
		impulseResolution(collisionNormal, a, b);
	}
	
	/**
	 * Resolves the collision using impulse resolution. Impulse resolution is a method of 
	 * collision resolution where an instantaneous change in velocity is 
	 * applied to each of the objects involved.
	 * 
	 * @param collisionNormal  the direction in which the collision should resolve
	 * @param a  the first entity
	 * @param b  the second entity
	 */
	public static void impulseResolution(Vector3f collisionNormal, Entity a, Entity b) {
			
		// store inverse masses
		float invMassA = 1f / a.getMass();
		float invMassB = 1f / b.getMass();
		
		// find velocity of B relative to A
		Vector3f relativeVel = new Vector3f();
		b.getVelocity().sub(a.getVelocity(), relativeVel);
			
		// take the dot product of the relative velocity and the collision normal
		float velAlongNormal = relativeVel.dot(collisionNormal);
			
		// resolve collision only if objects are moving towards each other
		if (velAlongNormal <= 0) {
				
			// get coefficient of restitution
			// this will be the smallest of the two objects
			float e = Math.min(-a.getCoefficientOfRestitution(), -b.getCoefficientOfRestitution());
				
			// calculate impulse scalar
			float impulseScalar = (-(1+e) * velAlongNormal) / (invMassA + invMassB);
				
			// calculate impulse
			Vector3f impulseA = new Vector3f();
			collisionNormal.mul(impulseScalar, impulseA);
				
			Vector3f impulseB = new Vector3f();
			collisionNormal.mul(impulseScalar, impulseB);
				
			// apply impulse
			a.getVelocity().sub(impulseA.div(a.getMass()));
			b.getVelocity().add(impulseB.div(b.getMass()));
			
			// apply friction
			// this method is called here to make use of the impulse scalar that was calculated
			friction(collisionNormal, a, b, impulseScalar);
		}
	}
	
	/**
	 * Computes the friction vector to be added to each entity in question. 
	 * There are two types of friction: static friction and kinetic friction. 
	 * Static friction is friction between the two objects when they are stationary 
	 * relative to each other. Dynamic friction is friction between the two 
	 * objects when they are sliding across each other.
	 * 
	 * @param collisionNormal  the direction in which the collision resolved
	 * @param a  the first entity
	 * @param b  the second entity
	 * @param j  the magnitude of the normal force
	 */
	public static void friction(Vector3f collisionNormal, 
			Entity a, Entity b, float j) {
		
		// store inverse masses
		float invMassA = 1f / a.getMass();
		float invMassB = 1f / b.getMass();
		
		// find velocity of B relative to A
		Vector3f relativeVel = new Vector3f();
		b.getVelocity().sub(a.getVelocity(), relativeVel);
		
		// solve for tangent vector
		Vector3f tangent = new Vector3f();
		collisionNormal.mul(relativeVel.dot(collisionNormal), tangent);		
		relativeVel.sub(tangent, tangent);
		
		if (tangent.length() != 0)
			tangent.normalize();
		
		// solve for magnitude and apply along friction vector
		float jt = -relativeVel.dot(tangent);
		jt /= invMassA + invMassB;
		
		// solve for static friction given the static friction coefficients 
		// of each object
		// this method uses the Pythagorean Theorem
		float staticFriction = pythagoreanTheorem(a.getStaticFriction(), 
				b.getStaticFriction());
		
		// clamp magnitude of friction and create impulse vector
		Vector3f frictionImpulse = new Vector3f();
		if (Math.abs(jt) < j * staticFriction) {
			tangent.mul(jt, frictionImpulse);
		}
		else {
			float kineticFriction = pythagoreanTheorem(a.getKineticFriction(), 
					b.getKineticFriction());
			
			tangent.mul(kineticFriction, frictionImpulse);
			frictionImpulse.mul(-j);
		}
		
		Vector3f temp = new Vector3f(frictionImpulse.x, frictionImpulse.y, frictionImpulse.z); 
		
		// apply friction impulse
		a.getVelocity().sub(temp.div(a.getMass()));
		b.getVelocity().add(frictionImpulse.div(b.getMass()));
	}
		
	/**
	 * Clamps the given float to the given extents. If x is smaller 
	 * than min, x will be set to min. If x is larger than max, x 
	 * will be set to max.
	 * 
	 * @param x
	 * @param min
	 * @param max
	 * @return x  after it has been clamped
	 */
	public static float clamp(float x, float min, float max) {
			
		if (x < min)
			x = min;
		else if ( x > max)
			x = max;
			
		return x;
	}
	
	/**
	 * Computes the value of c following the Pythagorean 
	 * Theorem: a^2 + b^2 = c^2.
	 * 
	 * @param a
	 * @param b
	 * @return c
	 */
	public static float pythagoreanTheorem(float a, float b) {
		
		float c = (float) (Math.pow(a, 2) + Math.pow(b, 2));
		c = (float) Math.sqrt(c);
		
		return c;
	}
	
}
