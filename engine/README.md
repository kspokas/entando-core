entando-core-engine
============

**Argon2 encryption algorithm**.
It provides a one-way encryption for passwords in the Entando Platform.

In order to use the new encryption methods in an existing project, you have to execute this ```alter table``` on your Serv DB (example for PostgreSql):
```ALTER TABLE authusers
   ALTER COLUMN passwd TYPE character varying(512);```

**Configuration**
You can configure the execution of the algorithm by editing the properties file ```security.properties```; you can find this file in ```src/main/config``` path of your project.
The default values of the settings are:

```algo.argon2.type=ARGON2i
algo.argon2.hash.length=32
algo.argon2.salt.length=16
algo.argon2.iterations=4
algo.argon2.memory=65536
algo.argon2.parallelism=4```

Correct values for ```algo.argon2.type``` are (case sensitive):
1. ARGON2d is faster and uses data-depending memory access;
2. ARGON2i default value, is slower but uses data-independent memory access;
3. ARGON2id is a hybrid of Argon2i and Argon2d, using a combination of data-depending and data-independent memory accesses;

Correct values for ```algo.argon2.hash.length``` are: 4..2^32-1 (longer hash means more complex and safe hash but slower execution)

Correct values for ```algo.argon2.salt.length``` are: 8..2^32-1 (longer salt means more complex and safe hash but slower execution)

Correct values for ```algo.argon2.iterations``` are: 1..2^32-1 (more iterations means more complex and safe hash but slower execution)

Correct values for ```algo.argon2.parallelism``` are: 8..2^32-1 (parallelism means the number of execution threads; more threads means more complex and safe hash but slower execution)

Correct values for ```algo.argon2.memory``` are: 8*parallelism..2^32-1 (greater memory means more complex and safe hash but slower execution)

**Entando** is the lightest, open source Digital Experience Platform (DXP) for Modern Applications. Entando harmonizes customer experience across the omnichannel (UX convergence) applying the techniques of modern software practices to enterprise applications (modern applications). Entando can be used to modernize UI/UX layers on top of existing applications or to build new generation applications aligned to UI/UX best practices, across different industries and use cases.

```entando-core```is composed of the three following main core components:

1. **engine**, includes core features and internal services of the platform
2. **admin-console**, includes tools to manage administrative core features and WCMS functionality
3. **portal-ui**, provides tools to create interactive web apps UI/UX

The Entando platform **v4.3.2-SNAPSHOT** project includes also the following Github projects:

* **entando-components**: https://github.com/entando/entando-components. Entando platform relies on a number of components or extensions that add functionality not included with the standard Entando platform. There are two types of components: Plugins and Bundles. Plugins extend the functionality of Entando engine, admin-console and portal-ui; Bundles extend the functionality of UI/UX Applications.

* **entando-archetypes**: https://github.com/entando/entando-archetypes. Entando archetypes project provides samples to kickstart your basic Entando application development and components.

* **entando-ux-packages**: https://github.com/entando/entando-ux-packages. Entando ux-packages are modules capable to implement a range of specific customizations.

See the [WIKI](https://github.com/entando/entando-core/wiki) pages for more project's information details. At the moment, the wiki pages are:

* [Getting Started](https://github.com/entando/entando-core/wiki/Getting-Started) page describes step-by-step instructions for running an application sample based on Entando.
* [Build from source code](https://github.com/entando/entando-core/wiki/Build-from-source-code) page describes step-by-step instructions   for installing projects and building from the latest source code.
* [How the Core works](https://github.com/entando/entando-core/wiki/How-the-Core-Works) page provides contents and architectural design to understand the Entando platform and its components.
* [How to use the Core](https://github.com/entando/entando-core/wiki/How-to-use-the-Core) page provides contents to start basic implementation.
* [FAQ](https://github.com/entando/entando-core/wiki/Faq) page provides all the answers to general questions.

You can contribute to our Open Source project, please read [GitHub guidelines](https://guides.github.com/activities/contributing-to-open-source/#contributing).

You can request bug fixes and new features on the [issues](https://github.com/entando/entando-core/issues) page.

For latest updated news, please visit the company website: https://www.entando.com.

Entando Core is released under [GNU Lesser General Public License](https://www.gnu.org/licenses/lgpl-2.1.txt) v2.1

Enjoy!

*The Entando Team*
