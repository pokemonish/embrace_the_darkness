module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        shell: {
            options: {
                stdout: true,
                stderr: true,
            },
            runServer: {
                command: 'java -cp server.jar main.Main'
            }
        },

        watch: {
            fest: {
                files: ['frontend/templates/**/*.xml'],
                tasks: ['fest'],
                options: {
                    interrupt: true,
                    livereload: true,
                }
            },
            server_tml: {
                files: [
                    'backend/server_tml/**/*'
                ],
                tasks: ['copy:server_tml'],
                options: {
                    interrupt: false,
                    livereload: false,
                }
            },
            public_html: {
                files: [
                    'public_html/**/*'
                ],
                options: {
                    interrupt: true,
                    livereload: true,
                }
            }
        },

        fest: {
            templates: {
                files: [{
                    expand: true,
                    cwd: 'frontend/templates',
                    src: '**/*.xml',
                    dest: 'public_html/js/tmpl'
                }],
                options: {
                    template: function (data) {
                        return grunt.template.process(
                            'define(function () {return <%= contents %> ; });',
                            {data: data}
                        );
                    }
                }
            }
        },

        copy: {
            server_tml: {
                expand: true,
                cwd: 'backend/server_tml/',
                src: '**',
                dest: 'server_tml/',
                flatten: false,
            }
        },

        concurrent: {
            target: ['watch', 'shell:runServer'],
            options: {
                logConcurrentOutput: true
            },
        },

        requirejs: {
            compile: {
                options: {
                    baseUrl: 'public_html/js',
                    name: 'main',
                    out: 'main-built.js'
                }
            }
        },

        sass: {
            options: {
                sourceMap: true,
            },
            dist: {
                files: {
                    'main.css': 'main.scss',
                }
            }
        },

    });


    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-shell-spawn');
    grunt.loadNpmTasks('grunt-fest');
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-concat-css');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');

    grunt.registerTask('default', ['copy', 'fest', 'concurrent']);
    grunt.registerTask('build', ['sass']);
}