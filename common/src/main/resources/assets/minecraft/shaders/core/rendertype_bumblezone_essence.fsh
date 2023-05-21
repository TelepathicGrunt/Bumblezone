#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform vec4 ColorModulator;
uniform float GameTime;
uniform int EndPortalLayers;

in vec4 vertexColor;
in vec4 texProj0;

const vec3[] COLORS = vec3[](
    vec3(0.925, 0.925, 0.925),
    vec3(0.75, 0.1, 0.1),
    vec3(0.65, 0.65, 0.1),
    vec3(0.1, 0.75, 0.1),
    vec3(0.1, 0.1, 0.75),
    vec3(0.7, 0.1, 0.6)
);

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat4 base_layer() {
    mat4 translate = mat4(
    1.0, 0.0, 0.0, 1.0,
    0.0, 1.0, 0.0, GameTime * 20.0,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
    );

    mat2 scale = mat2(8.0);

    return mat4(scale) * translate * SCALE_TRANSLATE;
}

mat4 bee_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / (layer + 1),
        0.0, 1.0, 0.0, (3.0 + (layer / 2.5)) * (GameTime * 160.0),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians(((layer * layer * 4321.0) + layer + 1) * 9.5));

    mat2 scale = mat2((4.5 - (layer / 3.0)) * 4.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, texProj0 * base_layer()).rgb * COLORS[0];
    for (int i = 0; i < EndPortalLayers; i++) {
        color += textureProj(Sampler1, texProj0 * bee_layer(float(i + 1))).rgb * COLORS[i + 1];
    }
    fragColor = vec4(color, 1.0) * vertexColor * ColorModulator;
}
