#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform vec4 ColorModulator;
uniform float GameTime;
uniform int EndPortalLayers;

in vec4 vertexColor;
in vec4 texProj0;
in vec3 view;

const vec3[] COLORS = vec3[](
    vec3(0.925, 0.925, 0.925),
    vec3(0.75, 0.1, 0.1),
    vec3(0.65, 0.65, 0.1),
    vec3(0.1, 0.75, 0.1),
    vec3(0.1, 0.1, 0.75),
    vec3(0.7, 0.1, 0.6)
);

vec2 angle2vec2(float radians) {
    return vec2(cos(radians), sin(radians));
}

vec3 base_layer() {
    float time = GameTime * 50.0;
    float scale = 0.2;
    mat3 translate = mat3(
        scale, 0.0, 0.0,
        0.0, scale, time,
        0.0, 0.0, scale
    );

    return -view.xyz * translate;
}

vec3 bee_layer(float layer) {
    float time = (GameTime * (100.0 + (layer * 10)));

    float scale = 0.2 - (layer / 50);
    float rotation = ((layer * layer * 4321.0) + layer + 1) * 9.5;
    vec2 dir_vec = angle2vec2(radians(rotation));
    mat3 rotateAndTranslate = mat3(
        dir_vec.x * scale, -dir_vec.y * scale, 0.0,
        dir_vec.y * scale, dir_vec.x * scale, time,
        0.0, 0.0, scale
    );

    return (-view.xyz * (layer + 1)) * rotateAndTranslate;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, base_layer()).rgb * COLORS[0];
    for (int i = 0; i < EndPortalLayers; i++) {
        color += textureProj(Sampler1, bee_layer(float(i + 1))).rgb * COLORS[i + 1];
    }
    fragColor = vec4(color, 1.0) * vertexColor * ColorModulator;
}
