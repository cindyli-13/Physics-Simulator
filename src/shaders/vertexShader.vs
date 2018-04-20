#version 330

in vec3 position;
in vec2 textures;

out vec2 passTextures;

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;

void main(void) {

	gl_Position = projectionMatrix * worldMatrix * vec4(position, 1.0);
	passTextures = textures;
}