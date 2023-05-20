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
    vec3(0.4, 0.4, 0.4),
    vec3(0.5, 0.15, 0.15),
    vec3(0.15, 0.5, 0.15),
    vec3(0.15, 0.15, 0.5),
    vec3(0.46, 0.17, 0.17),
    vec3(0.17, 0.46, 0.17),
    vec3(0.17, 0.17, 0.46),
    vec3(0.48, 0.16, 0.16)
);

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 2.5) * (GameTime * 20.0),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2((4.5 - layer / 3.0) * 1.2);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, texProj0).rgb * COLORS[0];
    for (int i = 0; i < EndPortalLayers; i++) {
        color += textureProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * COLORS[i];
    }
    fragColor = vec4(color, 1.0) * vertexColor * ColorModulator;
}
