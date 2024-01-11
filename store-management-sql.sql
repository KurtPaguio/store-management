PGDMP                          |            store-management    13.12    13.12     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    179012    store-management    DATABASE     t   CREATE DATABASE "store-management" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_Philippines.1252';
 "   DROP DATABASE "store-management";
                postgres    false            �            1259    179073    role    TABLE     �   CREATE TABLE public.role (
    id character varying NOT NULL,
    name character varying(255),
    description character varying(500)
);
    DROP TABLE public.role;
       public         heap    postgres    false            �            1259    179032    store    TABLE     g  CREATE TABLE public.store (
    id character varying NOT NULL,
    name character varying(500),
    address character varying(500),
    telephone_number numeric,
    email_address character varying(255),
    type character varying,
    date_created timestamp without time zone,
    date_modified timestamp without time zone,
    users_id character varying
);
    DROP TABLE public.store;
       public         heap    postgres    false            �            1259    179065    users    TABLE     S  CREATE TABLE public.users (
    id character varying NOT NULL,
    date_created timestamp without time zone,
    date_modified timestamp without time zone,
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255),
    email character varying(500),
    role character varying(255)
);
    DROP TABLE public.users;
       public         heap    postgres    false            �            1259    179081    users_roles    TABLE     u   CREATE TABLE public.users_roles (
    users_id character varying NOT NULL,
    role_id character varying NOT NULL
);
    DROP TABLE public.users_roles;
       public         heap    postgres    false            �          0    179073    role 
   TABLE DATA           5   COPY public.role (id, name, description) FROM stdin;
    public          postgres    false    202   �       �          0    179032    store 
   TABLE DATA           �   COPY public.store (id, name, address, telephone_number, email_address, type, date_created, date_modified, users_id) FROM stdin;
    public          postgres    false    200   t       �          0    179065    users 
   TABLE DATA           n   COPY public.users (id, date_created, date_modified, first_name, last_name, password, email, role) FROM stdin;
    public          postgres    false    201   �       �          0    179081    users_roles 
   TABLE DATA           8   COPY public.users_roles (users_id, role_id) FROM stdin;
    public          postgres    false    203   �       5           2606    179080    role role_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public            postgres    false    202            1           2606    179039    store store_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.store
    ADD CONSTRAINT store_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.store DROP CONSTRAINT store_pkey;
       public            postgres    false    200            3           2606    179072    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    201            7           2606    179088    users_roles users_roles_pkey 
   CONSTRAINT     i   ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (users_id, role_id);
 F   ALTER TABLE ONLY public.users_roles DROP CONSTRAINT users_roles_pkey;
       public            postgres    false    203    203            �   �   x�U��
�0 ��+��W^L�B:T���%�<j-���J�.��9?�H>C�uHCT�@0z��4&1m/����Q��\�C�~�X�1a�;�)��ʉ�|�t�I��\�{��Vd���X�i�0C
�P�L�0����>�\����$���]3�      �      x������ � �      �      x������ � �      �      x������ � �     